package acctMgr.model;

public class OverdrawException extends Exception {
	OverdrawException(double amt){
		super("Overdraw by " + amt);
	}
}
