package com.archie.tacocloud.tacointerfaces;

import com.archie.tacocloud.dom.Ingredient;

public interface IngredientRespository
{
	Iterable<Ingredient> findAll();
	Ingredient findOne(String id);
	Ingredient save(Ingredient ingredient);
}
