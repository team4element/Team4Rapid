package com.team4.robot.subsystems;

class SuperstructurePeriodicIO {

}

public class Superstructure extends Subsystem<SuperstructurePeriodicIO> {
	private static Superstructure mInstance = null;
	private SuperstructurePeriodicIO mPeriodicIO;
	private SuperstructureState mCurrentState = SuperstructureState.IDLE;

	private final Intake mIntake = Intake.getInstance();

	private Superstructure() {

	}

	public static enum SuperstructureState {
		IDLE,
		INTAKE_CONVEY,
		REVERSE,
	}

	public void setControlState(SuperstructureState newState) {
		mCurrentState = newState;
	}

	@Override
	public void onLoop(double timestamp) {
		// switch(mCurrentState) {
		// 	case IDLE:
		// 		mIntake.setControlState(IntakeState.IDLE);
		// 		break;
		// 	case INTAKE_CONVEY:
		// 		// TODO: Add Conveyer
		// 		mIntake.setControlState(IntakeState.FWD);
		// 		break;
		// 	case REVERSE:
		// 		mIntake.setControlState(IntakeState.REVERSE);
		// 		break;
		// 	default:
		// 		System.out.println("Invalid Superstructure State reached");
		// }
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisableLoop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readPeriodicInputs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writePeriodicOutputs() {
		// TODO Auto-generated method stub
		
	}
	
	public static Superstructure getInstance() {
		if (mInstance == null) {
			mInstance = new Superstructure();
		}

		return mInstance;
	}
}
