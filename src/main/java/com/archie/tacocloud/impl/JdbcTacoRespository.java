package com.archie.tacocloud.impl;

import com.archie.tacocloud.dom.Ingredient;
import com.archie.tacocloud.dom.Taco;
import com.archie.tacocloud.tacointerfaces.TacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRespository implements TacoRepository
{
	private JdbcTemplate jdbc;

	@Autowired
	public JdbcTacoRespository(JdbcTemplate jdbc)
	{
		this.jdbc = jdbc;
	}

	@Override
	public Taco save(final Taco taco)
	{
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		for (Ingredient ingredient : taco.getIngredients())
		{
			saveIngredientToTaco(ingredient, tacoId);
		}
		return taco;
	}

	private long saveTacoInfo(Taco taco)
	{
		taco.setCreatedDate(new Date());
		PreparedStatementCreatorFactory pfc =
				new PreparedStatementCreatorFactory("insert into Taco (name, createdAt) values (?, ?)", Types.VARCHAR,
						Types.TIMESTAMP);
		pfc.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc= pfc.newPreparedStatementCreator(
						Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedDate().getTime())));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);
		if(keyHolder.getKey() == null){
			System.out.println("no key captured");
		}
		return keyHolder.getKey().longValue();
	}

	private void saveIngredientToTaco(Ingredient ingredient, long tacoId)
	{
		jdbc.update("insert into Taco_Ingredients (taco, ingredient) " + "values (?, ?)", tacoId, ingredient.getId());
	}
}
