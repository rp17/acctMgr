package acctMgr.model;

import javax.swing.SwingUtilities;

public class WithdrawAgent extends AbstractModel implements Runnable, Agent {
	private Object pauseLock;
	private volatile boolean paused;
	public volatile boolean active;
	private Account account;
	private double amount;
	private double transferred;
	private String name = new String("Default");
	private AgentStatus status;
	private volatile boolean wasBlocked;
	
	public WithdrawAgent(Account account, double amount){
		this.account = account;
		this.amount = amount;
		this.transferred = 0;
		this.status = AgentStatus.Running;
		this.wasBlocked = false;
		this.active = true;
		this.paused = false;
		this.pauseLock = new Object();
	}
	public void run() {
		while(active) {
			synchronized (pauseLock) {
				
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                    	System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
                    }
                }
                
            }
			account.autoWithdraw(amount, this);
			this.transferred += amount;
			final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AmountTransferredUpdate, transferred, AgentStatus.NA);
			SwingUtilities.invokeLater(
					new Runnable() {
					    public void run() {
					    	notifyChanged(me);
					    }
					});
			try {
				Thread.sleep(500);
			}
			catch(InterruptedException ex){
				System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
			}
		}
	}
	public double getTransferred(){return transferred;}
	public void onPause() {
        synchronized (pauseLock) {
            paused = true;
            setStatus(AgentStatus.Paused);
        }
    }

    public void onResume() {
        synchronized (pauseLock) {
        	if(wasBlocked) setStatus(AgentStatus.Blocked);
        	else setStatus(AgentStatus.Running);
            paused = false;
            pauseLock.notify();
        }
    }
    public void setStatus(AgentStatus agSt) {
    	status = agSt;
    	if(status == AgentStatus.Blocked) wasBlocked = true;
    	if(status == AgentStatus.Running) wasBlocked = false;
    	final ModelEvent me = new ModelEvent(ModelEvent.EventKind.AgentStatusUpdate, 0.0, agSt);
    	SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
    }
    public AgentStatus getStatus(){return status;}
    public void setName(String name) {this.name = name;}
    public String getName(){return name;}
    public Account getAccount(){return account;}
    public void finish(){
    	active = false;
    }
}
