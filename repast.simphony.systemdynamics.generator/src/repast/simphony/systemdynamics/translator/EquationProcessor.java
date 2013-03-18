package repast.simphony.systemdynamics.translator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import repast.simphony.systemdynamics.support.MutableInteger;

public class EquationProcessor {

    public static HashSet<String> rhsvariables = new HashSet<String>(); // code generation
    public static HashSet<String> functions = new HashSet<String>(); // none
    public static HashSet<String> lookups = new HashSet<String>(); // tokenize
    public static HashSet<String> subscripts = new HashSet<String>();
    public static Map<String, List<String>> subscriptValues = new HashMap<String, List<String>>();
    
    private SystemDynamicsObjectManager sdObjectManager;
    
    boolean processingMacroCode = false;
    
    public EquationProcessor() {
    }

    public EquationProcessor(boolean processingMacroCode) {
	this.processingMacroCode = processingMacroCode;
    }

    public HashMap<String, Equation> processRawEquations(SystemDynamicsObjectManager sdObjectManager, List<String> rawEquations) {
	HashMap<String, Equation> equations = new HashMap<String, Equation>();
	this.sdObjectManager = sdObjectManager;
	
	String vensimEquation;
	String aLine;


	if (!processingMacroCode) {
	    // Need this here -- note that we explicity set Time = time in repeated loop during execution
	    Equation timeEquation = new Equation("Time=0.0~"+Translator.UNITS_PROPERTIES.getProperty("timeUnits")+
		    "~This is autogenerated");
	    equations.put(timeEquation.getLhs(), timeEquation);
	    Equation naEquation = new Equation("NAREPLACEMENT = 0.0000001~any~This is autogenerated");
	    equations.put(naEquation.getLhs(), naEquation);

	}

	// read all equations until we hit "\\\---///"


	// this loop extends to the botton of the method

	MutableInteger linePtr = new MutableInteger(0);

	while (true) {
	    int varPkgLine = 0;
	    String initialLineTerminator = "";
	    String insertedEqual = "";
	    aLine = rawEquations.get(linePtr.valueAndInc());
	    

	    // skip over empty lines prior to the start of an equation
	    while (aLine.length() == 0) {
		if (linePtr.value() >= rawEquations.size() && processingMacroCode)
		    return equations;
		aLine = rawEquations.get(linePtr.valueAndInc());
	    }
	    
	
	    // if the first line of the equation is continued onto additional lines
	    // we want to capture all lines and concatenate as a single line
	    if (aLine.contains("TABBED")) {
		
//		Sample Tabbed array equation:
//		We will translate this into a "normal" array initialization
//		Aggregated Definition[COP,Aggregated Regions]=1,0,0;1,0,0;...;0,0,1
//		Aggregated Definition[COP,Aggregated Regions]=TABBED ARRAY(
//			1	0	0
//			1	0	0
//			0	1	0
//			0	1	0
//			1	0	0
//			0	1	0
//			0	0	1
//			)
//			~	dmnl
//			~	Tabbed array to define to which aggregated region each of the COP blocs \
//				belongs.
//			|
		
		aLine = aLine.split("=", 2)[0]+"=";
		String anotherLine;
		anotherLine = rawEquations.get(linePtr.valueAndInc()).trim();
		while (!anotherLine.endsWith(")")) {
		    
		    String[] values = anotherLine.split("\t");
		    int vPtr = 0;
		    for (String value : values) {
			if (vPtr++ > 0)
			    aLine += ",";
			aLine += value;
			
		    }
		    aLine += ";";
		    anotherLine = rawEquations.get(linePtr.valueAndInc()).trim();
		}
			
			
	    } else {
		if (aLine.endsWith("\\")) {
		    String concat = aLine.replace("\\", "");
		    while (aLine.endsWith("\\")) {
			aLine = rawEquations.get(linePtr.valueAndInc());
			concat += aLine.replace("\\", "");
		    }
		    aLine = concat;
		}
	    }

	    // what can we learn from initial line?
	    boolean isAssignment = false, isLookup = false, isVdmLookup = false, isSubscriptDefinition = false,  unsure = true, isMulti = false;
	    boolean isMacroDefinition = false;

	    // is it an assignment statement?
	    // assignment has both left and right sides
	    if (aLine.contains("=")) {
		isAssignment = true;
		unsure = false;
		initialLineTerminator = aLine.substring(aLine.length()-1, aLine.length());
		
		// ordering is important. Much check for macro before since macro definition
		// meets criteria of non-quoted colon
	    } else if (aLine.contains(":MACRO:")) {
		unsure = false;
		isMacroDefinition = true;
		initialLineTerminator = aLine.substring(aLine.length()-1, aLine.length());
		// check if we have a subscript definition
	    } else if (Parser.containsNonquotedColon(aLine)
		    || aLine.contains("<->")) { 
		// nonQuotedColon
		isSubscriptDefinition = true;
		unsure = false;
		initialLineTerminator = ":";
		initialLineTerminator = aLine.substring(aLine.length()-1, aLine.length());
		// how about a lookup?
	    } else if (aLine.endsWith("(")) {
		// Note: there cannot be an = in statement, but that is caught
		// in initial comparison
		isLookup = true;
		unsure = false;
		initialLineTerminator = "(";
		// is it a multiline with first line a vdmlookup?
	    } else if (aLine.endsWith("~~|")) {
		// and multi-equation also
		isVdmLookup = true;
		isMulti = true;
		unsure = false;
		initialLineTerminator = "~~|";
		// need to add vdmlookup invocation in this case
		String lhs = aLine.replace("~~|", "").trim();
		//				    if (lhs.contains("["))
		//					    lhs += "array.";
		aLine = lhs + " = VDMLOOKUP(\""+lhs+"\")"+initialLineTerminator;
	    } else {
		// if not an assignment &&
		// not a subscript definition &&
		// not a lookup definition &&
		// not a multi-equation vmdlookup

		// may be a single equation vdmlookup
		// or a "special" assignment that doesn't have an =
		// need to look at the next line to determine

		unsure = true;

	    }

	    varPkgLine++;

	    // this is the beginning of the graphics, return as we do not do anything with the graphics
	    if (aLine.contains("---///"))
		return equations;
	    
	    // process macros independently
	    if (isMacroDefinition) {
//		System.err.println("Macros are currently not supported -- EXITING");
//		System.exit(1);
		processMacro(aLine, rawEquations, linePtr);
	    } else {
		
		// This is were we will need to deal with TABBED ARRAYS to read in properly.
		// Just convert into ArrayInitialization

		// start constructing the entire vensim equation
		vensimEquation = new String(aLine).replace("\\", "");
		int additionalLineCount = 0;

		// Now process the rest of the Variable Equation package
		// read until we hit the stand-alone "|"
		while (true) {

		    insertedEqual = "";

		    // start with the "second" line of the equation package
		    aLine = rawEquations.get(linePtr.valueAndInc());

		    // is this a continued line? if so read it in in its entirety
		    if (aLine.endsWith("\\")) {
			String concat = aLine.replace("\\", "");
			while (aLine.endsWith("\\")) {
			    aLine = rawEquations.get(linePtr.valueAndInc());
			    concat += aLine.replace("\\", "");
			}
			aLine = concat;
		    }

		    additionalLineCount++;
		    varPkgLine++;

		    // check for validity and termination conditions

		    if (aLine == null) {
			System.err.println("Unexpected EOF!!!");
		    }
		    if (aLine.contains("---///")) {
			System.err.println("Found graphics marker in wrong place!");
		    }
		    // are we finished with the package?
		    if (aLine.endsWith("|") && !aLine.endsWith("~~|")) { // used to be \t| the trim() on readLine() has thrown this off
			break;
		    }

		    // Note that each equation in a multi-equation package could be a different type!
		    // so we need to repeat the logic from above. This should really go into a separate
		    // method that will deal with everything 
		    //					This needs work


		    // we could determine at this point that this is actually a multi-equation package
		    if (aLine.endsWith("~~|")) {
			isMulti = true;
		    }
		    if (isMulti) {
			if (isVdmLookup) {
			    String lhs = aLine.replace("~~|", "").trim();
			    aLine = lhs + " = VDMLOOKUP(\""+lhs+"\")"+initialLineTerminator;
			}

		    } else {
			// special processing based on what we have from first line

			if (unsure) {
			    // may be a single equation vdmlookup
			    // or a "special" assignment that doesn't have an =
			    // need to look at the next line to determine
			    if (aLine.startsWith("~")) {
				isVdmLookup = true;
				unsure = false;
				// need to modify current vensimEquation
				// if this is an array reference, need to add array.
				//						if (vensimEquation.contains("["))
				//						    vensimEquation = "array." + vensimEquation;
				vensimEquation = vensimEquation + " = VDMLOOKUP(\""+vensimEquation+"\")";
			    } else {
				// this should be a number and thus an assignment
				isAssignment = true;
				unsure = false;
				insertedEqual = " = ";
			    }

			} else {

			}

		    }

		    vensimEquation = vensimEquation + insertedEqual + aLine.replace("\\", "");

		}

		// we now have the equation text
		// skip "equations" that begin with "*" -- section markers
		if (!vensimEquation.startsWith("***")) {

		    // trap any equations requiring special processing prior to creating
		    List<String> eqnToProcess = new ArrayList<String>();
		    //				    eqnToProcess.add(vensimEquation);
		    eqnToProcess = splitMultipleEquations(vensimEquation);
		    for (String aVensimEquation : eqnToProcess) {
			if (aVensimEquation.contains("WITH LOOKUP") /* || isLookup */) {
			    processWithLookup(aVensimEquation, equations);
			} else {
//			    System.out.println(aVensimEquation);
			    Equation equation = new Equation(aVensimEquation);
			    
			    

			    if (equation.getLhs() == null) {
				System.out.println("Equation with no LHS: "+aVensimEquation);
			    } else {
				equations.put(equation.getLhs(), equation);
				
				String screenName = SystemDynamicsObjectManager.getScreenName(equation.getLhs());
				if (equation.isAssignment())
				    sdObjectManager.addEquation(screenName, equation);
			    }
			}
		    }
		} else {
//		    System.out.println("Skipping <"+vensimEquation+">");
		}
	    }
	}	

    }



    private List<String> splitMultipleEquations(String vensimEquation) {
	List<String> eqn = new ArrayList<String>();

	if (vensimEquation.contains("~~|")) {

	    // split multi-equation definition into multiple single equation definitions

	    String[] parts = vensimEquation.split("~\t", 2);
	    String[] commands = parts[0].split("~~\\|");
	    String units = "\t~\t" + parts[1];

	    for (String cmd : commands) {

		eqn.add(cmd + units);
	    }


	    return eqn;
	} else {
	    eqn.add(vensimEquation);
	    return eqn;
	}
    }

    private void processMacro(String eqn, List<String> rawEquations, MutableInteger linePtr) {

	if (!MacroManager.readMacroAndProcess(eqn, rawEquations, linePtr)) {
	    System.out.println("Bad Macro");;
	}

    }

    private void processWithLookup(String eqn, HashMap<String, Equation> equations) {
	//		"Op: Effect of Fatigue on Capacity"= WITH LOOKUP (
	//		"Op: Fatigue Normalized",
	//			([(0,0)-(4,2)],(0,1.25),(0.35,1.2),(0.65,1.12),(1,1),(1.35,0.85),(1.65138,0.692982)\
	//			,(2,0.5),(2.5,0.25),(3,0.1),(4,0.1) ))


	// NOTE: if the LHS contains an array reference, we want to name the lookup table without the 
	// array reference being included

	// lhs
	String[] parts = eqn.split("=", 2);
	boolean withQuotes = false;
	if (parts[0].contains("\""))
	    withQuotes = true;
	String l = "";

	String lhsArrayReference = "";

	if (parts[0].contains("[")) {
	    String[] withAR = parts[0].split("\\[");
	    parts[0] = withAR[0];
	    lhsArrayReference = "["+withAR[1];
	}


	l = parts[0].replaceAll("\"", "");
	String newl = l + "_lookup";
	if (withQuotes)
	    newl = "\"" + newl + "\"";

	// if the rhs contains an array reference, then we cannot
	// split the whole thing on a ",". 
	// change the subscript commas to "###" and then convert back

	// need to scan the rhs changing any "," in an array reference to "###"
	// there can be any number

	StringBuffer rhsBuf = new StringBuffer();
	parts[1] = alterArrayReferenceCommas(parts[1]);

	String[] rhs = parts[1].split(",", 2);
	String newr = rhs[1].trim();
	newr = newr.substring(0, newr.length());

	Equation equation = new Equation(newl+newr);
	if (equation.getLhs() == null) {
	    System.out.println("Equation with no LHS: "+newl+newr);
	} else {
	    
	    String screenName = SystemDynamicsObjectManager.getScreenName(equation.getLhs());
		sdObjectManager.addEquation(screenName, equation);
	    equations.put(equation.getLhs(), equation);
	}
	// now reconstruct the original with a lookup reference

	String newEqn = parts[0]+lhsArrayReference + "="+newl+rhs[0].replaceAll("WITH LOOKUP", "").replaceAll("###", ",")+")";
	// need to grab units and comment from original rhs
	String[] extra = rhs[1].split("~");
	String comment = "";
	String units = "";
	if (extra.length > 1)
	    units = extra[1];
	if (extra.length > 2)
	    comment = extra[2];
	equation = new Equation(newEqn+"~"+units+"~"+comment);
	if (equation.getLhs() == null) {
	    System.out.println("Equation with no LHS: "+newEqn);
	} else {
	    equations.put(equation.getLhs(), equation);
	    
	    String screenName = SystemDynamicsObjectManager.getScreenName(equation.getLhs());
		sdObjectManager.addEquation(screenName, equation);
	}
    }

    private String alterArrayReferenceCommas(String rhs) {
	StringBuffer sb = new StringBuffer();
	boolean inArrayReference = false;
	boolean doneChecking = false;
	MutableInteger position = new MutableInteger(0);

	while (Parser.inRange(rhs, position)) {
	    if (Parser.characterAt(rhs, position).equals("[") && !doneChecking) {
		inArrayReference = true;
		sb.append(Parser.characterAt(rhs, position));
	    } else if (Parser.characterAt(rhs, position).equals("]")&& !doneChecking) {
		inArrayReference = false;
		sb.append(Parser.characterAt(rhs, position));
	    } else if (Parser.characterAt(rhs, position).equals(",")&& !doneChecking) {
		if (inArrayReference) {
		    inArrayReference = false;
		    sb.append("###");
		} else {
		    sb.append(",");
		    doneChecking = true;
		}
	    } else {
		sb.append(Parser.characterAt(rhs, position));
	    }
	    position.add(1);
	}

	return sb.toString();
    }
}
