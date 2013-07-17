package acctMgr.model;

public interface Agent extends Model {
	public double getTransferred();
	public void onPause();
	public void onResume();
	public void setName(String name);
	public String getName();
	public Account getAccount();
	public void setStatus(AgentStatus agSt);
	public AgentStatus getStatus();
	public void finish();
}
