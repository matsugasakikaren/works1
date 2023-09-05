package jp.co.works;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import jp.co.works.form.UpdateForm;

public class UpdateFormParam implements Serializable {
	@Valid
    private List<UpdateForm> formList;

	public void setFormList(List<UpdateForm> list) {
		this.formList = list;
	}

	public List<UpdateForm> getFormList() {
		return formList;
	}
}