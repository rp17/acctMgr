package acctMgr.model;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
public class Account extends AbstractModel {
	private double balance;
	
	public Account(double balance){
		this.balance = balance;
	}
	public double getBalance(){return balance;}
	public synchronized void deposit(double amount) {
		double oldB = balance;
		balance += amount;
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
		notifyAll();
	}
	
	public synchronized void withdraw(double amount) throws OverdrawException {
		double newB = balance - amount;
		if(newB < 0.0) throw new OverdrawException(newB);
		balance = newB;
		final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, balance, AgentStatus.NA);
		
		SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
	}
	public synchronized void autoWithdraw(double amount, Agent ag) {
		try {
			System.out.println("Trying to withdraw " + amount + " from balance " + balance);
			
			//if(balance - amount < 0.0) {
				//System.out.println("Sending blocked");
				//ag.setStatus(AgentStatus.Blocked);		
			//}
			
			while(this.balance - amount < 0.0) {
				ag.setStatus(AgentStatus.Blocked);	
				wait();
			}
			if(ag.getStatus() == AgentStatus.Paused) return;
			ag.setStatus(AgentStatus.Running);
					
			this.balance -= amount;
			final ModelEvent me = new ModelEvent(ModelEvent.EventKind.BalanceUpdate, this.balance, AgentStatus.Running);
			SwingUtilities.invokeLater(
				new Runnable() {
				    public void run() {
				    	notifyChanged(me);
				    }
				});
		}
		catch(InterruptedException ex){
			System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
		}
		/*
		catch(InvocationTargetException ex){
			System.out.println("Thread " + Thread.currentThread().getName() + " " + ex.getMessage());
			ex.printStackTrace();
		}
		*/
	}
}

