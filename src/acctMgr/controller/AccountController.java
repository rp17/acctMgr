package acctMgr.controller;

import acctMgr.model.Account;
import acctMgr.model.Agent;
import acctMgr.model.AgentCreator;
import acctMgr.model.OverdrawException;
import acctMgr.view.AccountView;
import acctMgr.view.AgentView;

import javax.swing.SwingUtilities;

public class AccountController extends AbstractController {

	public void operation(String opt) {
		if(opt == AccountView.Deposit) {
			double amount = ((AccountView)getView()).getAmount();
			((Account)getModel()).deposit(amount);
		} else if(opt == AccountView.Withdraw) {
			double amount = ((AccountView)getView()).getAmount();
			try {
				((Account)getModel()).withdraw(amount);
			}
			catch(OverdrawException ex) {
				final String msg = ex.getMessage();
				SwingUtilities.invokeLater(new Runnable() {
				      public void run() {
				    	  ((AccountView)getView()).showError(msg);
				      }
				    });
			}
		} else if(opt == AccountView.StartDepAgent) {
			final AccountView acView = (AccountView)getView();
			double amount = acView.getAmount();
			final Agent ag = AgentCreator.createDepAgent(((Account)getModel()), amount);
			final AgentController agContr = new AgentController();
			
			agContr.setModel(ag);
			SwingUtilities.invokeLater(new Runnable() {
			      public void run() {
			    	  acView.createAgentView(ag, agContr);
			      }
			    });
		} else if(opt == AccountView.StartWithdrawAgent) {
			final AccountView acView = (AccountView)getView();
			double amount = acView.getAmount();
			final Account accnt = (Account)getModel();
			final Agent ag = AgentCreator.createWithdrawAgent(((Account)getModel()), amount);
			final AgentController agContr = new AgentController();
			agContr.setModel(ag);
			SwingUtilities.invokeLater(new Runnable() {
			      public void run() {
			    	  AgentView app = acView.createAgentView(ag, agContr);
			    	  accnt.addModelListener(app);
			      }
			    });
		}
	}
}
