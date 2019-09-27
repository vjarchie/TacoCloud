package com.archie.tacocloud.controller;

import com.archie.tacocloud.dom.Ingredient;
import com.archie.tacocloud.dom.Order;
import com.archie.tacocloud.dom.Taco;
import com.archie.tacocloud.tacointerfaces.IngredientRespository;
import com.archie.tacocloud.tacointerfaces.TacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import com.archie.tacocloud.dom.Ingredient.Type;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController
{
	private final IngredientRespository ingredientRepo;

	private TacoRepository tacoRepository;

	@Autowired
	public DesignTacoController(IngredientRespository ingredientRepo,TacoRepository tacoRepository){
		this.ingredientRepo = ingredientRepo;
		this.tacoRepository = tacoRepository;
	}

	@ModelAttribute(name = "order")
	public Order order(){
		return new Order();
	}

	@ModelAttribute(name = "taco")
	public Taco taco(){
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model){
//		List<Ingredient> ingredientList = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla",Type.WRAP),
//				new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//				new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//				new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//				new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//				new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//				new Ingredient("CHED", "Cheddar", Type.CHEESE),
//				new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//				new Ingredient("SLSA", "Salsa", Type.SAUCE),
//				new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
		List<Ingredient> ingredientList = new ArrayList<>();
		ingredientRepo.findAll().forEach(ingredient -> ingredientList.add(ingredient));
		Type[] types = Ingredient.Type.values();
		for(Type type:types){
			model.addAttribute(type.toString().toLowerCase(),filterByType(ingredientList, type));
		}
		model.addAttribute("design",new Taco());
		return "design";
	}

	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors,@ModelAttribute Order order) {
		if (errors.hasErrors()) {
			return "design";
		}
		log.info("Processing design: " + design);
		Taco saved = tacoRepository.save(design);
		order.addDesign(saved);

		return "redirect:/orders/current";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type)
	{
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}


}
