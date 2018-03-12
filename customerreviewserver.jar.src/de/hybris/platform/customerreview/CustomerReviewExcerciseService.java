package de.hybris.platform.customerreview.impl;


import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
/**
 * 
 * @author hsingh
 *
 */
public interface CustomerReviewExcerciseService {
	
	/**
	 * It gives total number of customer reviews whose ratings are within a given range minRating(inclusive) and maxRating(inclusive)
	 * 
	 * throws exception if minRating or maxRating is null or maxRating < minRating
	 * 
	 * @param product 
	 * @param minRating
	 * @param maxRating
	 * @return
	 * @throws JaloInvalidParameterException
	 */
	Integer getNumberOfReviews(ProductModel product, Double minRating, Double maxRating) throws JaloInvalidParameterException;
	
	/**
	 * 
	 * It gives total number of customer reviews whose ratings are within a range (inclusive) specified by Hybris Config file with defaults defined under CustomerReviewConstants 
	 * I know this was probably not needed. I saw existing code trying to read from config file with some defaults under CustomerReviewConstants.
	 * if the expectation is to reuse hybris way of doing things then i guess this would be way forward
	 * 
	 * throws exception if minRating or maxRating is null or maxRating < minRating
	 * 
	 * @param product 
	 * @param minRating
	 * @param maxRating
	 * @return
	 * @throws JaloInvalidParameterException
	 */
	Integer getNumberOfReviews(ProductModel product) throws JaloInvalidParameterException;

	/**
	 * It creates a customer review if comment  does not contains curse words defined in spring bean xml and rating >=0  
	 * . 
	 * @param rating
	 * @param headline
	 * @param comment
	 * @param user
	 * @param product
	 * @return
	 * @throws JaloInvalidParameterException
	 */
	CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user,
			ProductModel product) throws JaloInvalidParameterException;
}
