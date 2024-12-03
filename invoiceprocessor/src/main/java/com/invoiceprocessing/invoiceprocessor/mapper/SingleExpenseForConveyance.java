package com.invoiceprocessing.invoiceprocessor.mapper;

import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.VOUCHER;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.ConveyanceVoucher;
import com.invoiceprocessing.invoiceprocessor.model.CostCode;
import com.invoiceprocessing.invoiceprocessor.model.Project;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.VoucherCosts;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.model.VoucherProjects;
import com.invoiceprocessing.invoiceprocessor.repository.ConveyanceVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.CostCodeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.ProjectRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherCostsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherProjectsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.CostCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.ProjectDto;
import com.invoiceprocessing.invoiceprocessor.response.ReferenceDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.service.WorkflowServiceImpl;

@Component("conveyanceForSingleExpense")
public class SingleExpenseForConveyance implements VoucherWithOutTripMapper<VoucherWithOutTripDto, VoucherEntity> {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ConveyanceVoucherRepository conveyanceVoucherRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	WorkflowServiceImpl workflowServiceImpl;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	VoucherProjectsRepository voucherProjectsRepository;

	@Autowired
	VoucherCostsRepository voucherCostsRepository;

	@Autowired
	CostCodeRepository costCodeRepository;

	@Override
	public VoucherEntity dtoToModelWithOutTrip(VoucherWithOutTripDto singleExpenseConveyanceVoucherDto) {
		// TODO Auto-generated method stub
		Voucher voucher = new Voucher();
		ConveyanceVoucher conveyanceVoucher = new ConveyanceVoucher();
		VoucherProjects voucherProjects = null;
		List<VoucherProjects> listOfVoucherProjects = new ArrayList<VoucherProjects>();
		VoucherCosts voucherCosts = null;
		List<VoucherCosts> listOfVoucherCosts = new ArrayList<VoucherCosts>();
		insertDataInConveyanceModel(singleExpenseConveyanceVoucherDto, voucher, conveyanceVoucher);
		voucher = voucherRepository.save(voucher);

		if (!singleExpenseConveyanceVoucherDto.getProjectCodes().isEmpty()) {
			for (ProjectDto projectCode : singleExpenseConveyanceVoucherDto.getProjectCodes()) {

				voucherProjects = new VoucherProjects();
				voucherProjects.setVoucher(voucher);
				voucherProjects.setClaimedAmount(new BigDecimal(projectCode.getAmount()));
				Project project = projectRepository.findByProjectCode(projectCode.getProjectCode());
				voucherProjects.setProject(project);

				listOfVoucherProjects.add(voucherProjects);

			}
			voucherProjectsRepository.saveAll(listOfVoucherProjects);
		}

		if (!singleExpenseConveyanceVoucherDto.getCostCodes().isEmpty()) {
			for (CostCodeDto costCode : singleExpenseConveyanceVoucherDto.getCostCodes()) {

				voucherCosts = new VoucherCosts();
				voucherCosts.setVoucher(voucher);
				voucherCosts.setClaimedAmount(new BigDecimal(costCode.getAmount()));
				CostCode costCodeEntity = costCodeRepository.findByCostCode(costCode.getCostCode());
				voucherCosts.setCostCode(costCodeEntity);

				listOfVoucherCosts.add(voucherCosts);

			}
			voucherCostsRepository.saveAll(listOfVoucherCosts);
		}

		conveyanceVoucher = conveyanceVoucherRepository.save(conveyanceVoucher);
		ReferenceDto reference = new ReferenceDto();
		reference.setReferenceId(voucher.getVoucherID());
		reference.setReferenceType(VOUCHER);
		workflowServiceImpl.processFirstWorkflowTask(reference);
		return conveyanceVoucher;
	}

	@Override
	public VoucherWithOutTripDto modelToDtoWithOutTrip(VoucherEntity target) {
		// TODO Auto-generated method stub
		return null;
	}

	private void insertDataInConveyanceModel(VoucherWithOutTripDto singleExpenseConveyanceVoucherDto, Voucher voucher,
			ConveyanceVoucher conveyanceVoucher) {
		// TODO Auto-generated method stub
		voucher.setClaimedAmount(new BigDecimal(singleExpenseConveyanceVoucherDto.getTotalAmount() == null ? "0.0"
				: singleExpenseConveyanceVoucherDto.getTotalAmount().replace(",", "")));
		voucher.setExceptionReason(singleExpenseConveyanceVoucherDto.getExceptionReason() == null ? "No Exception"
				: singleExpenseConveyanceVoucherDto.getExceptionReason());
		voucher.setFileName(singleExpenseConveyanceVoucherDto.getFilename());
		voucher.setPaymentMode(singleExpenseConveyanceVoucherDto.getPaymentMode());
		voucher.setHashText(singleExpenseConveyanceVoucherDto.getHashedText());
		voucher.setInvoiceType(singleExpenseConveyanceVoucherDto.getBillType());
		voucher.setInvoiceNumber(singleExpenseConveyanceVoucherDto.getInvoiceNo());
		voucher.setTotalAmount(new BigDecimal(singleExpenseConveyanceVoucherDto.getTotalAmount()));
		voucher.setMerchantName(singleExpenseConveyanceVoucherDto.getMerchantName());
		voucher.setVoucherDate(parseStringToDate(singleExpenseConveyanceVoucherDto.getDate()));
		voucher.setInvoiceDate(parseStringToDate(singleExpenseConveyanceVoucherDto.getDate()));
		voucher.setFileImage(singleExpenseConveyanceVoucherDto.getImage());
		voucher.setManualEntry(singleExpenseConveyanceVoucherDto.getManualEntry());
		voucher.setIsSingleExpense(singleExpenseConveyanceVoucherDto.getIsSingleExpense());
		voucher.setEmployeeID(employeeRepository.findById(singleExpenseConveyanceVoucherDto.getEmployeeId()).get());

//		....................changes made here...................
//		if (!singleExpenseConveyanceVoucherDto.getTripId().isEmpty())
//			voucher.setEmployeeID(singleExpenseConveyanceVoucherDto
//					.findById(Integer.parseInt(singleExpenseConveyanceVoucherDto.getTripId())).get().getEmployeeID());

		conveyanceVoucher
				.setDistance(new BigDecimal(singleExpenseConveyanceVoucherDto.getNoOfKm().equalsIgnoreCase("Not Found")
						|| singleExpenseConveyanceVoucherDto.getNoOfKm().equalsIgnoreCase("") ? "0.0"
								: singleExpenseConveyanceVoucherDto.getNoOfKm()));
		conveyanceVoucher.setFromLocation(singleExpenseConveyanceVoucherDto.getFromLocation());
		conveyanceVoucher.setModeOfTravel(singleExpenseConveyanceVoucherDto.getModeOfTravel());
		conveyanceVoucher.setToLocation(singleExpenseConveyanceVoucherDto.getToLocation());
		conveyanceVoucher.setTravelClass(singleExpenseConveyanceVoucherDto.getTravelClass());
		conveyanceVoucher.setVoucherID(voucher);
		conveyanceVoucher
				.setArrivalDate(singleExpenseConveyanceVoucherDto.getArrivalDate().equalsIgnoreCase("Not Found")
						|| singleExpenseConveyanceVoucherDto.getArrivalDate().equalsIgnoreCase("") ? getCurrentDate()
								: parseStringToDate(singleExpenseConveyanceVoucherDto.getArrivalDate()));
//		conveyanceVoucher.setArrivalTime(parseTime(singleExpenseConveyanceVoucherDto.getArrivalTime()));
//		conveyanceVoucher.setDepartureTime(parseTime(singleExpenseConveyanceVoucherDto.getArrivalTime()));
		conveyanceVoucher.setArrivalTime(singleExpenseConveyanceVoucherDto.getArrivalTime() != null
				? singleExpenseConveyanceVoucherDto.getArrivalTime()
				: "");
		conveyanceVoucher.setDepartureTime(singleExpenseConveyanceVoucherDto.getDepartureTime() != null
				? singleExpenseConveyanceVoucherDto.getDepartureTime()
				: "");
		conveyanceVoucher.setInterIntraCity(singleExpenseConveyanceVoucherDto.getIntraOrInterCityTravel());
		conveyanceVoucher.setVehicleType(singleExpenseConveyanceVoucherDto.getVehicleType());
		conveyanceVoucher.setVehicleFuelType(singleExpenseConveyanceVoucherDto.getVehicleFuelType());
		conveyanceVoucher
				.setDepartureDate(singleExpenseConveyanceVoucherDto.getDepartureDate().equalsIgnoreCase("Not Found")
						|| singleExpenseConveyanceVoucherDto.getDepartureDate().equalsIgnoreCase("") ? getCurrentDate()
								: parseStringToDate(singleExpenseConveyanceVoucherDto.getDepartureDate()));
	}

	public static Time parseTime(String inputTime) {
		final String TIME_FORMAT_24HR = "HH:mm";
		final String TIME_FORMAT_24HRSS = "HH:mm:ss";
		final String TIME_FORMAT_12HR = "hh:mm a";
		final String TIME_FORMAT_12HRSS = "hh:mm:ss a";
		SimpleDateFormat sdf = null;

		try {
			sdf = new SimpleDateFormat(TIME_FORMAT_24HR);
			return new Time(sdf.parse(inputTime).getTime());
		} catch (ParseException e) {
			try {
				sdf = new SimpleDateFormat(TIME_FORMAT_24HRSS);
				return new Time(sdf.parse(inputTime).getTime());
			} catch (ParseException e1) {

				try {
					sdf = new SimpleDateFormat(TIME_FORMAT_12HR);
					return new Time(sdf.parse(inputTime).getTime());
				} catch (ParseException e2) {
					try {
						sdf = new SimpleDateFormat(TIME_FORMAT_12HRSS);
						return new Time(sdf.parse(inputTime).getTime());
					} catch (ParseException e3) {
						// Invalid format - to add logger error
					}
				}
			}
		}

		return null;
	}

	private static Date getCurrentDate() {
		java.sql.Date dateSql = null;
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = dateObj.format(formatter);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			java.util.Date utilDate = format.parse(date);
			dateSql = new java.sql.Date(utilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateSql;
	}

	private static Date parseStringToDate(String date) {
		Date outDate = null;
		try {
			outDate = new java.sql.Date(((java.util.Date) new SimpleDateFormat("dd-MM-yyyy").parse(date)).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return outDate;
	}

}
