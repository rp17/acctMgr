package acctMgr.view;

import acctMgr.controller.AgentController;
import acctMgr.controller.Controller;
import acctMgr.controller.AccountController;
import acctMgr.model.Agent;
import acctMgr.model.Model;
import acctMgr.model.Account;
import acctMgr.model.ModelEvent;
import acctMgr.model.AgentCreator;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class AccountView extends JFrameView {
	public final static String Deposit = "Deposit";
	public final static String Withdraw = "Withdraw";
	public final static String StartDepAgent = "StartDepAgent";
	public final static String StartWithdrawAgent = "StartWithdrawAgent";
	private JPanel topPanel;
	private JPanel textPanel;
	private JPanel buttonPanel;
	private JLabel balanceLabel;
	private JLabel amountLabel;
	private JTextPane balanceField;
	private JTextPane amountField;
	private JButton depButton;
	private JButton withdrawButton;
	private JButton startDepAgentButton;
	private JButton startWithdrawAgentButton;
	
	private Handler handler = new Handler();
	private List<AgentController> agentContrs = new ArrayList<AgentController>(10);
	
	private AccountView(Model model, Controller controller){
		super(model, controller);
		this.getContentPane().add(getContent());
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		int x = (int) ((dim.getWidth() - this.getWidth()) * 0.5f);
	    int y = (int) ((dim.getHeight() - this.getHeight()) * 0.5f);
	    this.setLocation(x, y);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent evt) {
		    	for(AgentController agContr : agentContrs) agContr.operation(AgentView.Dismiss);
		    	AgentCreator.finishThreads();
		        dispose();
		        System.exit(0);
		    }
		});
		pack();
	}
	private JPanel getContent() {
		if (topPanel == null) {
			topPanel = new JPanel();
			GridLayout layout = new GridLayout(2, 1);
			topPanel.setLayout(layout);
			//topPanel.setPreferredSize(new Dimension(300, 100));
			GridBagConstraints ps = new GridBagConstraints();
			ps.gridx = 0;
			ps.gridy = 0;
			ps.fill = GridBagConstraints.HORIZONTAL;
			
			GridBagConstraints bs = new GridBagConstraints();
			bs.gridx = 0;
			bs.gridy = 1;
			topPanel.add(getTextFieldPanel(), null);
			topPanel.add(getButtonPanel(), null);
		}
		return topPanel;
	}
	private JButton getDepButton(){
		if(depButton == null){
			depButton = new JButton(Deposit);
			depButton.addActionListener(handler);
		}
		return depButton;
	}
	private JButton getWithdrawButton(){
		if(withdrawButton == null){
			withdrawButton = new JButton(Withdraw);
			withdrawButton.addActionListener(handler);
		}
		return withdrawButton;
	}
	private JButton getDepAgentButton(){
		if(startDepAgentButton == null){
			startDepAgentButton = new JButton(StartDepAgent);
			startDepAgentButton.addActionListener(handler);
		}
		return startDepAgentButton;
	}
	private JButton getWithdrawAgentButton(){
		if(startWithdrawAgentButton == null){
			startWithdrawAgentButton = new JButton(StartWithdrawAgent);
			startWithdrawAgentButton.addActionListener(handler);
		}
		return startWithdrawAgentButton;
	}
	private JPanel getButtonPanel()
	{
		if(buttonPanel == null){
			GridBagConstraints depButtonCtr = new GridBagConstraints();
			depButtonCtr.gridx = 0;
			depButtonCtr.gridy = 0;
			
			GridBagConstraints wButtonCtr = new GridBagConstraints();
			wButtonCtr.gridx = 1;
			wButtonCtr.gridy = 0;
			
			GridBagConstraints depAgButtonCtr = new GridBagConstraints();
			depAgButtonCtr.gridx = 2;
			depAgButtonCtr.gridy = 0;
			
			GridBagConstraints wAgButtonCtr = new GridBagConstraints();
			wAgButtonCtr.gridx = 3;
			wAgButtonCtr.gridy = 0;
			
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getDepButton(), depButtonCtr);
			buttonPanel.add(getWithdrawButton(), wButtonCtr);
			buttonPanel.add(getDepAgentButton(), depAgButtonCtr);
			buttonPanel.add(getWithdrawAgentButton(), wAgButtonCtr);
		}
		
		return buttonPanel;
	}
	private JPanel getTextFieldPanel()
	{
		if(textPanel == null){
			GridBagConstraints bl = new GridBagConstraints();
			bl.gridx = 0;
			bl.gridy = 0;
			
			GridBagConstraints bf = new GridBagConstraints();
			bf.gridx = 1;
			bf.gridy = 0;
			
			GridBagConstraints aml = new GridBagConstraints();
			aml.gridx = 0;
			aml.gridy = 1;
			
			GridBagConstraints amf = new GridBagConstraints();
			amf.gridx = 1;
			amf.gridy = 1;
			
			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			//textPanel.setPreferredSize(new Dimension(250, 50));
			textPanel.add(getBalanceLabel(), bl);
			textPanel.add(getBalanceField(), bf);
			textPanel.add(getAmountLabel(), aml);
			textPanel.add(getAmountField(), amf);
			
		}
		return textPanel;
	}
	private JLabel getBalanceLabel(){
		if(balanceLabel == null){
			balanceLabel = new JLabel();
			balanceLabel.setText("Balance:");
			balanceLabel.setPreferredSize(new Dimension(200, 20));
		}
		return balanceLabel;
	}
	
	private JTextPane getBalanceField(){
		if(balanceField == null){
			balanceField = new JTextPane();
			balanceField.setText(Double.toString(((Account)getModel()).getBalance()));
			balanceField.setSize(200, 25);
			balanceField.setEditable(false);
		}
		return balanceField;
	}
	private JLabel getAmountLabel(){
		if(amountLabel == null){
			amountLabel = new JLabel();
			amountLabel.setText("Amount:");
			amountLabel.setPreferredSize(new Dimension(200, 20));
		}
		return amountLabel;
	}
	
	private JTextPane getAmountField(){
		if(amountField == null){
			amountField = new JTextPane();
			amountField.setText(Double.toString(((Account)getModel()).getBalance()*0.01));
			amountField.setSize(200, 25);
			amountField.setEditable(true);
		}
		return amountField;
	}

	public double getAmount() {
		double amount = 0;
		// no checking for parsing errors
		amount = Double.parseDouble(amountField.getText());
		return amount;
	}
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
	public void modelChanged(ModelEvent me){
		if(me.getKind() == ModelEvent.EventKind.BalanceUpdate) {
			System.out.println("Balance field to " + me.getBalance());
			balanceField.setText(Double.toString(me.getBalance()));
		}
	}
	public AgentView createAgentView(Agent ag, AgentController agContr){
		AgentView app = new AgentView(ag, agContr);
  	  	agContr.setView(app);
  	  	agentContrs.add(agContr);
  	  	app.setVisible(true);
  	  	return app;
	}
	
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			((AccountController)getController()).operation(evt.getActionCommand());
		}
	}
	public static void main(String[] args) {
		final Account account = new Account(300.0);
		final AccountController contr = new AccountController();
		contr.setModel(account);
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	    	  AccountView app = new AccountView(account, contr);
	    	  contr.setView(app);
	    	  app.setVisible(true);
	      }
	    });
	  }
}
