package com.invoiceprocessing.invoiceprocessor.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.mapper.ConsolidatedVoucherMapperImpl;
import com.invoiceprocessing.invoiceprocessor.mapper.VoucherMapper;
import com.invoiceprocessing.invoiceprocessor.model.Voucher;
import com.invoiceprocessing.invoiceprocessor.model.VoucherEntity;
import com.invoiceprocessing.invoiceprocessor.repository.InvoiceConsolidatedSummeryRepository;
import com.invoiceprocessing.invoiceprocessor.repository.TripRepository;
import com.invoiceprocessing.invoiceprocessor.response.ConsolidatedBreakageSummaryDto;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.ConsolidatedVoucherService;
import com.invoiceprocessing.invoiceprocessor.utils.*;

@Service
public class ConsolidatedVoucherServiceImpl implements ConsolidatedVoucherService {

	@Autowired
	TripRepository tripRepository;

	@Autowired
	InvoiceConsolidatedSummeryRepository invoiceConsolidatedSummeryRepository;

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public List<ConsolidatedBreakageSummaryDto> saveAllConsolidatedData(List<VoucherDto> listOfVoucherDtos) {
		List<ConsolidatedBreakageSummaryDto> listOfConsolidatedSummaryDto = null;
		for (VoucherDto voucherDtoEntity : listOfVoucherDtos) {
			if (voucherDtoEntity.getManualEntry().equalsIgnoreCase("Y")) {
				@SuppressWarnings("unchecked")
				VoucherMapper<VoucherDto, VoucherEntity> voucherMapperForManualEntry = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
						.getBean(XpendeskConstants.SUBMIT_MANUAL_ENTRY);
				Voucher voucher = (Voucher) voucherMapperForManualEntry.dtoToModel(voucherDtoEntity);
				ConsolidatedBreakageSummaryDto consolidatedBreakageSummaryDto = new ConsolidatedBreakageSummaryDto();
				consolidatedBreakageSummaryDto.setVoucherID(voucher.getVoucherID().toString());
				if (voucherDtoEntity.getBillType().equalsIgnoreCase(XpendeskConstants.FOOD_TYPE))
					consolidatedBreakageSummaryDto
							.setFoodVoucherId(voucher.getFoodVoucherID().getFoodVoucherID().toString());
				else if (voucherDtoEntity.getBillType().equalsIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE)) {
					consolidatedBreakageSummaryDto.setConveyanceVoucherId(
							voucher.getConveyanceVoucherID().getConveyanceVoucherID().toString());
				} else {
					consolidatedBreakageSummaryDto
							.setHotelVoucherId(voucher.getHotelVoucherID().getHotelVoucherID().toString());
				}

				listOfConsolidatedSummaryDto = new ArrayList<ConsolidatedBreakageSummaryDto>();

				listOfConsolidatedSummaryDto.add(consolidatedBreakageSummaryDto);

			} else {
				if (StringUtils.equalsAnyIgnoreCase(XpendeskConstants.HOTEL_TYPE, voucherDtoEntity.getBillType())) {
					@SuppressWarnings("unchecked")
					VoucherMapper<VoucherDto, VoucherEntity> voucherMapperForHotel = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
							.getBean(XpendeskConstants.SUBMIT_FOR_HOTEL);
					voucherMapperForHotel.dtoToModel(voucherDtoEntity);
				} else if (StringUtils.equalsAnyIgnoreCase(XpendeskConstants.FOOD_TYPE,
						voucherDtoEntity.getBillType())) {
					@SuppressWarnings("unchecked")
					VoucherMapper<VoucherDto, VoucherEntity> voucherMapperForFood = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
							.getBean(XpendeskConstants.SUBMIT_FOR_FOOD);
					voucherMapperForFood.dtoToModel(voucherDtoEntity);
				} else if (StringUtils.equalsAnyIgnoreCase(XpendeskConstants.CONVEYANCE_TYPE,
						voucherDtoEntity.getBillType())) {
					@SuppressWarnings("unchecked")
					VoucherMapper<VoucherDto, VoucherEntity> voucherMapperForFood = (VoucherMapper<VoucherDto, VoucherEntity>) applicationContext
							.getBean(XpendeskConstants.SUBMIT_FOR_CONVEYANCE);
					voucherMapperForFood.dtoToModel(voucherDtoEntity);
				}
			}
		}
		if (!listOfVoucherDtos.get(0).getManualEntry().equalsIgnoreCase("Y")) {

			Integer tripID = (listOfVoucherDtos == null || listOfVoucherDtos.isEmpty()) ? 0
					: listOfVoucherDtos.stream().findFirst().map((x) -> Integer.parseInt(x.getTripId())).get();
			ConsolidatedVoucherMapperImpl consolidatedVoucherMapperImpl = (ConsolidatedVoucherMapperImpl) applicationContext
					.getBean(XpendeskConstants.CONSOLIDATION_TYPE);
			listOfConsolidatedSummaryDto = consolidatedVoucherMapperImpl.modelToDto(tripID);

		}
		return listOfConsolidatedSummaryDto;
//		return new ArrayList<ConsolidatedBreakageSummaryDto>();
	}

//	private Trip getTripByTripId(Integer tripId) {
//		Optional<Trip> tripEntity = tripRepository.findById(tripId);
//		return tripEntity.get();
//	}

}
