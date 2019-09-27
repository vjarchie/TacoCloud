package com.archie.tacocloud.component;

import com.archie.tacocloud.dom.Ingredient;
import com.archie.tacocloud.tacointerfaces.IngredientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient>
{
	private IngredientRespository ingredientRepo;

	@Autowired
	public IngredientByIdConverter(IngredientRespository ingredientRepo){
		this.ingredientRepo=ingredientRepo;
	}

	@Override
	public Ingredient convert(final String s)
	{
		return ingredientRepo.findOne(s);
	}
}
