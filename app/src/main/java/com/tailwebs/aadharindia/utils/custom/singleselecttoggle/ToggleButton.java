package com.tailwebs.aadharindia.utils.custom.singleselecttoggle;

import android.widget.Checkable;

public interface ToggleButton extends Checkable {

    void setOnCheckedChangeListener(OnCheckedChangeListener listener);

}
