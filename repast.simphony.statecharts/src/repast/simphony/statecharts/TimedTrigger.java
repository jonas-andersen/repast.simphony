package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;


public class TimedTrigger extends AbstractTrigger{

	private final double time;
	private double initializedTickCount;
	
	public TimedTrigger(double time){
		this.time = time;
	}
	
	@Override
	public boolean isRecurring() {
		return false;
	}
	
	@Override
	public double getInterval(){
		return 0;
	}
	
	@Override
	public void initialize(){
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		initializedTickCount = schedule.getTickCount();
	}
	
	@Override
	public double getNextTime() {
		return initializedTickCount + time;
	}
	
	@Override
	public boolean isTriggered(){
		return isValid();
	}
	
	@Override
	public boolean isValid(){
		return Double.compare(RunEnvironment.getInstance().getCurrentSchedule().getTickCount(),getNextTime()) >= 0;
	}
	
	public String toString(){
		return "TimedTrigger with time: " + time;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return false;
	}

	@Override
	public boolean isQueueConsuming() {
		return false;
	}
	
}
