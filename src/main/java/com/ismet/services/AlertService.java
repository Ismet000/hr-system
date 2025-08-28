package com.ismet.services;

import java.util.Map;

public interface AlertService {
    Map<String,Object> runDailySweep() throws Exception; // {overdueApprovals:[], forgottenClockOut:[], excessiveDaily:[]}

}
