package tests.android.base;

import constants.FakerConstants;
import tests.android.config.AndroidConfig;
import tests.android.pages.android.AndroidElementsPage;
import tests.android.pages.invitro.CartPage;
import tests.android.pages.invitro.InvitroElementsPage;
import tests.android.pages.invitro.MedicalTestElementsPage;

public class PageManager extends AndroidConfig {

    protected CartPage cart = new CartPage();
    protected AndroidElementsPage android = new AndroidElementsPage();
    protected InvitroElementsPage invitro = new InvitroElementsPage();
    protected MedicalTestElementsPage medicalTest = new MedicalTestElementsPage();

    protected FakerConstants fakerConstants = new FakerConstants();

}
