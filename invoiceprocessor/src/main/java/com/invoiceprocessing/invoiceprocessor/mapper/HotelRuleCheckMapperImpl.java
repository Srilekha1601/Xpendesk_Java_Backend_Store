package com.invoiceprocessing.invoiceprocessor.mapper;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Configuration;

@Configuration("accommodation")
public class HotelRuleCheckMapperImpl implements RuleCheckMapper {

	private final KieServices kieServices = KieServices.Factory.get();

	@Override
	public KieContainer checkRules() {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		kieFileSystem.write(ResourceFactory.newClassPathResource("rules/hoteleligibility.xls"));
		KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
		kb.buildAll();
		KieModule kieModule = kb.getKieModule();
		return kieServices.newKieContainer(kieModule.getReleaseId());
	}

}
