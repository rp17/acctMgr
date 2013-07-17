package acctMgr.model;

import java.util.List;
import java.util.ArrayList;
public class AgentCreator {
	private static int depAgCnt = 0;
	private static int withdrawAgCnt = 0;
	private static List<Thread> agentThreads = new ArrayList<Thread>(10);
	public static Agent createDepAgent(Account account, double amount) {
		DepositAgent depAg = new DepositAgent(account, amount);
		Thread depAgT = new Thread(depAg);
		String name = "Deposit Agent " + depAgCnt;
		depAg.setName(name);
		depAgT.setName(name);
		depAgCnt++;
		agentThreads.add(depAgT);
		depAgT.start();
		return depAg;
	}
	public static Agent createWithdrawAgent(Account account, double amount) {
		WithdrawAgent wAg = new WithdrawAgent(account, amount);
		Thread wAgT = new Thread(wAg);
		String name = "Withdraw Agent " + withdrawAgCnt;
		wAg.setName(name);
		wAgT.setName(name);
		withdrawAgCnt++;
		agentThreads.add(wAgT);
		wAgT.start();
		return wAg;
	}
	public static void finishThreads(){
		for(Thread t: agentThreads) {
				System.out.println("Finishing thread " + Thread.currentThread().getName());
				t.interrupt();
			}
	}
}
