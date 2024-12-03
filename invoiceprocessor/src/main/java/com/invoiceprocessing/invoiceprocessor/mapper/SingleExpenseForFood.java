package com.invoiceprocessing.invoiceprocessor.mapper;

import static com.invoiceprocessing.invoiceprocessor.utils.XpendeskConstants.VOUCHER;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.CostCode;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucher;
import com.invoiceprocessing.invoiceprocessor.model.FoodVoucherDetail;
import com.invoiceprocessing.invoiceprocessor.model.Project;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.VoucherCosts;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.model.VoucherProjects;
import com.invoiceprocessing.invoiceprocessor.repository.CostCodeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherDetailRepository;
import com.invoiceprocessing.invoiceprocessor.repository.FoodVoucherRepository;
import com.invoiceprocessing.invoiceprocessor.repository.ProjectRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherCostsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherProjectsRepository;
import com.invoiceprocessing.invoiceprocessor.repository.VoucherRepository;
import com.invoiceprocessing.invoiceprocessor.response.CostCodeDto;
import com.invoiceprocessing.invoiceprocessor.response.ProjectDto;
import com.invoiceprocessing.invoiceprocessor.response.ReferenceDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDetailDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherWithOutTripDto;
import com.invoiceprocessing.invoiceprocessor.service.WorkflowServiceImpl;

@Component("foodForSingleExpense")
public class SingleExpenseForFood implements VoucherWithOutTripMapper<VoucherWithOutTripDto, VoucherEntity> {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	FoodVoucherRepository foodVoucherRepository;

	@Autowired
	FoodVoucherDetailRepository foodVoucherDetailRepository;

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
	public VoucherEntity dtoToModelWithOutTrip(VoucherWithOutTripDto singleVoucherDto) {
		// TODO Auto-generated method stub
		Voucher voucher = new Voucher();
		FoodVoucher foodVoucher = new FoodVoucher();
		FoodVoucherDetail foodVoucherDetailsForSingleExpense = null;
		List<FoodVoucherDetail> listOfFoodVoucherDetails = new ArrayList<FoodVoucherDetail>();
		VoucherProjects voucherProjects = null;
		List<VoucherProjects> listOfVoucherProjects = new ArrayList<VoucherProjects>();
		VoucherCosts voucherCosts = null;
		List<VoucherCosts> listOfVoucherCosts = new ArrayList<VoucherCosts>();
		insertDataInModel(singleVoucherDto, voucher, foodVoucher, foodVoucherDetailsForSingleExpense,
				listOfFoodVoucherDetails);
		Voucher savedVoucher = voucherRepository.save(voucher);

		if (!singleVoucherDto.getProjectCodes().isEmpty()) {
			for (ProjectDto projectCode : singleVoucherDto.getProjectCodes()) {

				voucherProjects = new VoucherProjects();
				voucherProjects.setVoucher(savedVoucher);
				voucherProjects.setClaimedAmount(new BigDecimal(projectCode.getAmount()));
				Project project = projectRepository.findByProjectCode(projectCode.getProjectCode());
				voucherProjects.setProject(project);

				listOfVoucherProjects.add(voucherProjects);

			}
			voucherProjectsRepository.saveAll(listOfVoucherProjects);
		}

		if (!singleVoucherDto.getCostCodes().isEmpty()) {
			for (CostCodeDto costCode : singleVoucherDto.getCostCodes()) {

				voucherCosts = new VoucherCosts();
				voucherCosts.setVoucher(savedVoucher);
				voucherCosts.setClaimedAmount(new BigDecimal(costCode.getAmount()));
				CostCode costCodeEntity = costCodeRepository.findByCostCode(costCode.getCostCode());
				voucherCosts.setCostCode(costCodeEntity);

				listOfVoucherCosts.add(voucherCosts);

			}
			voucherCostsRepository.saveAll(listOfVoucherCosts);
		}

		// if manualEntry != N then:
		foodVoucher = foodVoucherRepository.save(foodVoucher);
		if (!listOfFoodVoucherDetails.isEmpty())
			foodVoucherDetailRepository.saveAll(listOfFoodVoucherDetails);
		ReferenceDto reference = new ReferenceDto();
		reference.setReferenceId(savedVoucher.getVoucherID());
		reference.setReferenceType(VOUCHER);
		workflowServiceImpl.processFirstWorkflowTask(reference);
		return savedVoucher;
	}

	@Override
	public VoucherWithOutTripDto modelToDtoWithOutTrip(VoucherEntity target) {
		// TODO Auto-generated method stub
		return null;
	}

	private void insertDataInModel(VoucherWithOutTripDto singleVoucherDto, Voucher voucher, FoodVoucher foodVoucher,
			FoodVoucherDetail foodVoucherDetail, List<FoodVoucherDetail> listOfFoodVoucherDetails) {
		// TODO Auto-generated method stub
		voucher.setFileName(singleVoucherDto.getFilename());
		voucher.setInvoiceType(singleVoucherDto.getBillType());
		voucher.setTotalAmount(new BigDecimal(singleVoucherDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
				: singleVoucherDto.getTotalAmount().replace(",", "")));
		voucher.setMerchantName(singleVoucherDto.getMerchantName());
		Long utilDate = new java.util.Date().getTime();
		voucher.setVoucherDate(
				singleVoucherDto.getDate().replace("/", " ").equalsIgnoreCase("Not Found") ? new java.sql.Date(utilDate)
						: parseStringToDate(singleVoucherDto.getDate()));
		voucher.setInvoiceDate(parseStringToDate(singleVoucherDto.getDate()));
		voucher.setExceptionReason(singleVoucherDto.getExceptionReason());
		voucher.setInvoiceNumber(singleVoucherDto.getInvoiceNo());
		voucher.setPaymentMode(singleVoucherDto.getPaymentMode());
		voucher.setIsSingleExpense(singleVoucherDto.getIsSingleExpense());
		voucher.setHashText(singleVoucherDto.getHashedText());
		voucher.setClaimedAmount(
				new BigDecimal(
						singleVoucherDto.getClaimedAmount() == null
								? singleVoucherDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
										: singleVoucherDto.getTotalAmount()
								: singleVoucherDto.getClaimedAmount()));
		voucher.setFileImage(singleVoucherDto.getImage());
		voucher.setManualEntry(singleVoucherDto.getManualEntry());
		voucher.setEmployeeID(employeeRepository.findById(singleVoucherDto.getEmployeeId()).get());

		foodVoucher.setVoucherID(voucher);
		foodVoucher.setContainsLiquor(Boolean.parseBoolean(
				singleVoucherDto.getLiquorStatus().equalsIgnoreCase("Liquor is Present") ? "True" : "False"));
		foodVoucher.setObjectionRaise(
				singleVoucherDto.getObjectionRaise() != null ? singleVoucherDto.getObjectionRaise() : "");
		foodVoucher
				.setTotalAmount(new BigDecimal(singleVoucherDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
						: singleVoucherDto.getTotalAmount().replace(",", "")));
		foodVoucher.setCgstAmount(singleVoucherDto.getCgstAmount() == null ? "0.0" : singleVoucherDto.getCgstAmount());
		foodVoucher.setSgstAmount(singleVoucherDto.getSgstAmount() == null ? "0.0" : singleVoucherDto.getSgstAmount());
		foodVoucher.setGstAmount(new BigDecimal(
				singleVoucherDto.getGstNo().equalsIgnoreCase("N/A") || singleVoucherDto.getGstNo().equalsIgnoreCase("")
						? "0.0"
						: singleVoucherDto.getGstNo().replace(",", "")));
		foodVoucher
				.setClaimedAmount(new BigDecimal(singleVoucherDto.getClaimedAmount() == null
						? singleVoucherDto.getTotalAmount().equalsIgnoreCase("Not Found") ? "0.0"
								: singleVoucherDto.getTotalAmount().replace(",", "")
						: singleVoucherDto.getClaimedAmount()));

		for (VoucherDetailDto foodVoucherDetails : singleVoucherDto.getConsolidatedHotelServiceBreakage()) {
			foodVoucherDetail = new FoodVoucherDetail();
			foodVoucherDetail.setFoodVoucherID(foodVoucher);
			foodVoucherDetail.setAmount(new BigDecimal(foodVoucherDetails.getAmount()));
			foodVoucherDetail.setBillType(foodVoucherDetails.getServiceType());
			foodVoucherDetail.setDescription(foodVoucherDetails.getDescription());
			foodVoucherDetail.setCgstAmount(new BigDecimal(foodVoucherDetails.getGst1()));
			foodVoucherDetail.setSgstAmount(new BigDecimal(foodVoucherDetails.getGst2()));
			foodVoucherDetail.setGstAmount(
					new BigDecimal(foodVoucherDetails.getGst1()).add(new BigDecimal(foodVoucherDetails.getGst2())));
			listOfFoodVoucherDetails.add(foodVoucherDetail);
		}

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
