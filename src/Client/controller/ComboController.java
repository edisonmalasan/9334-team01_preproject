package Client.controller;

import Client.model.ComboModel;

public class ComboController {
    private ComboModel comboModel;

    public ComboController() {
        this.comboModel = new ComboModel();
    }

    public void correctAnswer() {
        comboModel.incrementCombo();
    }

    public void wrongAnswer() {
        comboModel.resetCombo();
    }

    public int getComboCount() {
        return comboModel.getComboCount();
    }

    public int getHighestCombo() {
        return comboModel.getHighestCombo();
    }

}
