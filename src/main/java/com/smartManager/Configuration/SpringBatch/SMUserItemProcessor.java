package com.smartManager.Configuration.SpringBatch;

import org.springframework.batch.item.ItemProcessor;

import com.smartManager.Entity.SMContactEntity;

public class SMUserItemProcessor implements ItemProcessor<SMContactEntity, SMContactEntity>{

	@Override
	public SMContactEntity process(SMContactEntity item) throws Exception {
		return null;
	}

}
