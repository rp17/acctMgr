package acctMgr.controller;
import acctMgr.view.View;
import acctMgr.model.Model;

public interface Controller {
	void setModel(Model model);
	Model getModel();
	View getView();
	void setView(View view);
}
