package net.vpnsdk.vpn;

import java.util.ArrayList;

public class AAAMethod {
    private String mName;
    private String mDescription;
    private ArrayList<VPNManager.InputField> mList = new ArrayList<VPNManager.InputField>();

    AAAMethod(String name, String desc) {
        mName = name;
        mDescription = desc;
    }

    void addItem(VPNManager.InputField field) {
        if (field != null) {
            mList.add(field);
        }
    }

    /**
     * Return the name of the corresponding authentication method.
     *
     * @return a string represents the name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Return the description of the corresponding authentication method.
     *
     * @return a string represents the description.
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Return the InputField at the specified location in this method.
     *
     * @param index the index of the InputField to return.
     * @return the InputField at the specified index.
     */
    public VPNManager.InputField getInputField(int index) {
        return mList.get(index);
    }

    /**
     * Return the number of InputFields within this method.
     *
     * @return the number of InputFields within this method.
     */
    public int getInputFieldCount() {
        return mList.size();
    }
}
