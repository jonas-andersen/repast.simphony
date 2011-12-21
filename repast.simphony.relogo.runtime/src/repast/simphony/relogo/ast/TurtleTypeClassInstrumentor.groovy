package repast.simphony.relogo.ast

import java.beans.Introspector;
import java.text.DateFormat 
import java.text.SimpleDateFormat 
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassHelper 
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.ModuleNode 
import org.codehaus.groovy.ast.Parameter 
import org.codehaus.groovy.ast.VariableScope 
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.ConstantExpression 
import org.codehaus.groovy.ast.expr.MethodCallExpression 
import org.codehaus.groovy.ast.expr.VariableExpression 
import org.codehaus.groovy.ast.stmt.BlockStatement 
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.control.CompilationUnit 
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.CompilerConfiguration 
import org.objectweb.asm.Opcodes 

class TurtleTypeClassInstrumentor {
	private ClassNode turtleClass;
	private List<ClassNode> listOfUserClasses;
	private String singularString;
	private String pluralString;
	private String className;
	private List<ClassNode> listOfUserTurtleClasses;
	private List<ClassNode> listOfUserPatchClasses;
	private List<ClassNode> listOfUserLinkClasses;
	private List<ClassNode> listOfUserObserverClasses;
	
	public TurtleTypeClassInstrumentor(ClassNode turtleClass, List<ClassNode> listOfUserClasses, List<ClassNode> listOfUserTurtleClasses, 
			List<ClassNode> listOfUserPatchClasses, List<ClassNode> listOfUserLinkClasses, List<ClassNode> listOfUserObserverClasses,  
			String singularString, String pluralString){
		this.turtleClass = turtleClass;
		this.listOfUserClasses = listOfUserClasses;
		this.listOfUserTurtleClasses = listOfUserTurtleClasses;
		this.listOfUserPatchClasses = listOfUserPatchClasses;
		this.listOfUserLinkClasses = listOfUserLinkClasses;
		this.listOfUserObserverClasses = listOfUserObserverClasses;
		this.singularString = singularString;
		this.pluralString = pluralString;
		this.className = turtleClass.getName()
	}
	
	public instrument(){
//		log("about to add the turtle methods")
		// Turtle
		/*List<MethodNode> turtleMethodList = [
				turtleHatchTypesMethod(),
				turtlePatchTypesHereMethod(),
				turtlePatchTypesAtMethod(),
				tplTypesOnPMethod(),
				tplTypesOnTMethod(),
				tplTypesOnAMethod(),
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod()]*/
//		log("turtle methods are:")
//		turtleMethodList.each{
//			log("    $it")
//		}
		
		for (ClassNode cn : listOfUserTurtleClasses ){
			List<MethodNode> turtleMethodList = [
				turtleHatchTypesMethod(),
				turtlePatchTypesHereMethod(),
				turtlePatchTypesAtMethod(),
				tplTypesOnPMethod(),
				tplTypesOnTMethod(),
				tplTypesOnAMethod(),
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod()]
			for (MethodNode mn in turtleMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
		
		
		// Observers
		
		
		for (ClassNode cn in listOfUserObserverClasses){
			List<MethodNode> observerMethodList = [
				observerCreateTypesMethod(),
				observerCreateOrderedTypesMethod(),
				observerIsTypeQMethod(),
				observerTypesMethod(),
				observerTypeMethod(),
				observerTypesOnPMethod(),
				observerTypesOnTMethod(),
				observerTypesOnAMethod()]
			for (MethodNode mn in observerMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
		
		// Patches
		
		for (ClassNode cn in listOfUserPatchClasses){
			List<MethodNode> patchMethodList = [
				patchSproutTypesMethod(),
				turtlePatchTypesHereMethod(),
				turtlePatchTypesAtMethod(),
				tplTypesOnPMethod(),
				tplTypesOnTMethod(),
				tplTypesOnAMethod(),
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod()]
			for (MethodNode mn in patchMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
		
		// Links
		
		for (ClassNode cn in listOfUserLinkClasses){
			List<MethodNode> linkMethodList = [
				tplIsTypeQMethod(),
				tplTypesMethod(),
				tplTypeMethod(),
				tplTypesOnPMethod(),
				tplTypesOnTMethod(),
				tplTypesOnAMethod()]
			for (MethodNode mn in linkMethodList){
				if (mn){
					addMethod(cn,mn)
				}
			}
		}
	}
	
	private void addMethod(ClassNode cn, MethodNode mn){
		if (!cn.getMethod(mn.getName(), mn.getParameters())){
			cn.addMethod(mn)
		}
	}
	
	protected MethodNode observerCreateTypesMethod(){
		Parameter[] parameters = [new Parameter(ClassHelper.int_TYPE,'number'),new Parameter(ClassHelper.CLOSURE_TYPE,'closure',ConstantExpression.NULL)]
		ExpressionStatement es = new ExpressionStatement(
				new MethodCallExpression(
				new VariableExpression('this'),
				'crt',
				new ArgumentListExpression(
				new VariableExpression('number'),
				new VariableExpression('closure'),
				new ConstantExpression(pluralString)
				)
				)
				)
		BlockStatement block = new BlockStatement([es],new VariableScope())
		return( new MethodNode('create' + capitalize(pluralString), Opcodes.ACC_PUBLIC, ClassHelper.VOID_TYPE, parameters, [] as ClassNode[], block) );
	}
	
	protected MethodNode observerCreateOrderedTypesMethod(){
		String methodName = 'createOrdered' + capitalize(pluralString)
		String methodString = """
					public void ${methodName}(int number, Closure closure = null){
						this.cro(number,closure,'${pluralString}')
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtleHatchTypesMethod(){
		String methodName = 'hatch' + capitalize(pluralString)
		//log("    $methodName")
		String methodString = """
					public void ${methodName}(int number, Closure closure = null){
						this.hatch(number,closure,'${pluralString}')
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode patchSproutTypesMethod(){
		String methodName = 'sprout' + capitalize(pluralString)
		
		String methodString = """
					public void ${methodName}(int number, Closure closure = null){
						this.sprout(number,closure,'${pluralString}')
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtlePatchTypesHereMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'Here'
		//log("    $methodName")
		String methodString = """
					import repast.simphony.relogo.*
					import repast.simphony.space.grid.Grid
					import repast.simphony.space.grid.GridPoint
					public AgentSet ${methodName}(){
						Grid grid = getMyObserver().getGrid();
						GridPoint gridPoint = grid.getLocation(this);
						return Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),'${singularString}');
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode turtlePatchTypesAtMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'At'
		//log("    $methodName")
		String methodString = """
					import repast.simphony.relogo.*
					import repast.simphony.space.grid.GridPoint
					import repast.simphony.space.SpatialException
					public AgentSet ${methodName}(Number nX, Number nY){
						double dx = nX.doubleValue()
						double dy = nY.doubleValue()
						double[] displacement = [dx,dy];
						try{
						GridPoint gridPoint = Utility.getGridPointAtDisplacement(getTurtleLocation(),displacement,getMyObserver());
						return Utility.getTurtlesOnGridPoint(gridPoint,getMyObserver(),'${singularString}');
						}
						catch(SpatialException e){
							return null;
						}
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypesOnPMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'On'
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(Patch p){
						return Utility.getTurtlesOnGridPoint(p.getGridLocation(),getMyObserver(),'${singularString}');
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypesOnPMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'On'
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(Patch p){
						return Utility.getTurtlesOnGridPoint(p.getGridLocation(),this,'${singularString}');
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypesOnTMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'On'
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(Turtle t){
						return Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),getMyObserver(),'${singularString}');
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypesOnTMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'On'
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(Turtle t){
						return Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(t.getTurtleLocation()),this,'${singularString}');
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypesOnAMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'On'
		String methodString = """
					import repast.simphony.relogo.*
					import repast.simphony.space.continuous.NdPoint
					import repast.simphony.space.grid.GridPoint
					public AgentSet ${methodName}(AgentSet a){
						if (a == null || a.isEmpty()){
							return new AgentSet();
						}
						
						Set total = new HashSet();
						if (a.get(0) instanceof Turtle){
							for (Turtle t : a){
								NdPoint location = t.getTurtleLocation();
								AgentSet temp = Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(location),getMyObserver(),'${singularString}');
								total.addAll(temp);
							}
							
						}
						else {
							for (Patch p : a){
								GridPoint location = p.getGridLocation();
								AgentSet temp = Utility.getTurtlesOnGridPoint(location,getMyObserver(),'${singularString}');
								total.addAll(temp);
							}
							
						}
						return new AgentSet(total);
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypesOnAMethod(){
		String methodName = Introspector.decapitalize(pluralString) + 'On'
		String methodString = """
					import repast.simphony.relogo.*
					import repast.simphony.space.continuous.NdPoint
					import repast.simphony.space.grid.GridPoint
					public AgentSet ${methodName}(AgentSet a){
						if (a == null || a.isEmpty()){
							return new AgentSet();
						}
						
						Set total = new HashSet();
						if (a.get(0) instanceof Turtle){
							for (Turtle t : a){
								NdPoint location = t.getTurtleLocation();
								AgentSet temp = Utility.getTurtlesOnGridPoint(Utility.ndPointToGridPoint(location),this,'${singularString}');
								total.addAll(temp);
							}
							
						}
						else {
							for (Patch p : a){
								GridPoint location = p.getGridLocation();
								AgentSet temp = Utility.getTurtlesOnGridPoint(location,this,'${singularString}');
								total.addAll(temp);
							}
							
						}
						return new AgentSet(total);
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplIsTypeQMethod(){
		String methodName = 'is' + capitalize(singularString) + 'Q'
		String methodString = """
					import repast.simphony.relogo.*
					public boolean ${methodName}(Object o){
						Class clazz = getMyObserver().getTurtleFactory().getTurtleTypeClass('$singularString')
						return (clazz.isAssignableFrom(o))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerIsTypeQMethod(){
		String methodName = 'is' + capitalize(singularString) + 'Q'
		String methodString = """
					import repast.simphony.relogo.*
					public boolean ${methodName}(Object o){
						Class clazz = getTurtleFactory().getTurtleTypeClass('$singularString')
						return (clazz.isAssignableFrom(o))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypesMethod(){
		String methodName = Introspector.decapitalize(pluralString)
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						return (Utility.getTurtleAgentSetOfType('${pluralString}',this.getMyObserver()))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode tplTypeMethod(){
		String methodName = Introspector.decapitalize(singularString)
		String methodString = """
					import repast.simphony.relogo.*
					public Turtle ${methodName}(int num){
						Turtle turtle = Utility.turtle(num,this.getMyObserver())
						Class clazz = getMyObserver().getTurtleFactory().getTurtleTypeClass('$singularString')
						if (clazz.isAssignableFrom(turtle)) {
							return turtle
						}
						return null
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypesMethod(){
		String methodName = Introspector.decapitalize(pluralString)
		String methodString = """
					import repast.simphony.relogo.*
					public AgentSet ${methodName}(){
						return (Utility.getTurtleAgentSetOfType('${pluralString}',this))
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode observerTypeMethod(){
		String methodName = Introspector.decapitalize(singularString)
		String methodString = """
					import repast.simphony.relogo.*
					public Turtle ${methodName}(int num){
						Turtle turtle = Utility.turtle(num,this)
						Class clazz = getTurtleFactory().getTurtleTypeClass('$singularString')
						if (clazz.isAssignableFrom(turtle)) {
							return turtle
						}
						return null
					}
					"""
		return createMethodFromString(methodName,methodString)
	}
	
	protected MethodNode createMethodFromString(String methodName, String methodString){
//		log("    in createMethodFromString: $methodName")
		def result
		try {
		result = buildFromString(CompilePhase.SEMANTIC_ANALYSIS,false,methodString)
		}
		catch(Exception e){
//			log("    exception is $e")
//			log(e.printStackTrace())
		}
//		log("    ${className} in createMethodFromString, after building: $methodName")
		if (result){
			result[1].getMethods().each{
//				log("method : " + it)
			}
			return( result[1].getMethods().find({ it.name.equals(methodName) }))
		}
		return null
	}
	
	private List<ASTNode> buildFromString(CompilePhase phase = CompilePhase.CLASS_GENERATION, boolean statementsOnly = true, String source) {
		if (!source || "" == source.trim()) throw new IllegalArgumentException("A source must be specified")
		return compile(source, phase, statementsOnly);
	}
	
	private List<ASTNode> compile(String script, CompilePhase compilePhase, boolean statementsOnly) {
		def scriptClassName = "script" + className + System.currentTimeMillis()
		GroovyClassLoader classLoader = new GroovyClassLoader(this.getClass().getClassLoader())
		GroovyCodeSource codeSource = new GroovyCodeSource(script, scriptClassName + ".groovy", "/groovy/script")
		CompilationUnit cu = new CompilationUnit(CompilerConfiguration.DEFAULT, codeSource.codeSource, classLoader)
		cu.addSource(codeSource.getName(), script);
		cu.compile(compilePhase.getPhaseNumber())
		// collect all the ASTNodes into the result, possibly ignoring the script body if desired
		return cu.ast.modules.inject([]) {List acc, ModuleNode node ->
			if (node.statementBlock) acc.add(node.statementBlock)
			node.classes?.each {
				if (!(it.name == scriptClassName && statementsOnly)) {
					acc << it
				}
			}
			acc
		}
	}
	
	//Utility for capitalizing the first character of a string 
	private String capitalize(String s){
		String result = ''
		if (s){
			if (s.size() > 1){
				result = s[0].toUpperCase() + s[1..-1]
			}
			else {
				result = s.toUpperCase()
			}
		}
		return result
	}
	
	File file
	String fileLocation = 'outTurtleTypes.txt'
	
	private void log(def o){
		if (!file){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");
			Date date = new Date();
			def timeStamp = dateFormat.format(date);
			file = new File(fileLocation)
			file.append('\n')
			file.append(timeStamp)
			file.append('\n')
		}
		file.append(o)
		file.append('\n')
	}

}