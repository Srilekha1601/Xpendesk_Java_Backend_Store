package com.invoiceprocessing.invoiceprocessor.utils;

public class XpendeskConstants {

	private XpendeskConstants() {
	}

	public static final String HOTEL_TYPE = "Hotel";
	public static final String FOOD_TYPE = "Food";
	public static final String CONVEYANCE_TYPE = "Conveyance";

//	this is for consolidated voucher submit details
	public static final String SUBMIT_FOR_HOTEL = "hotelVoucherMapper";
	public static final String SUBMIT_FOR_FOOD = "foodVoucherMapper";
	public static final String SUBMIT_FOR_CONVEYANCE = "conveyanceVoucherMapper";
	public static final String SUBMIT_MANUAL_ENTRY = "manual";

//	To Get the consolidation Bean
	public static final String CONSOLIDATION_TYPE = "consolidation";

//	Attributes Of the consolidated voucher details 
	public static final String ACCOMMODATION_TYPE = "Accommodation";
	public static final String OUTHER_TYPE = "Outher";

//	Authentication Header 
	public static final String AUTHHEADER = "Authorization";

//	Units for workflow approval
	public static final String TRIP = "T";
	public static final String VOUCHER = "V";

//	Workflow action types
	public static final String SUBMITTED = "S";
	public static final String APPROVED = "A";
	public static final String REJECTED = "R";
	public static final String SENT_BACK = "B";
	public static final String IN_WORKFLOW = "W";
	public static final String PART_APPROVAL = "P";

//	Define Mark as Read Status as 'N'
	public static final String MARK_AS_READ = "N";

//	Select Cost Type Of the Project
	public static final String SELECTED_PROJECT_COST_TYPE_FOR_PROJECT = "project_code";
	public static final String SELECTED_PROJECT_COST_TYPE_FOR_COST = "cost_code";

//	set approval status for "T" and "F"
	public static final String VOUCHER_APPROVAL = "T";
	public static final String VOUCHER_REJECT = "F";

}
