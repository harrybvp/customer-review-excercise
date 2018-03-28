package de.hybris.platform.customerreview.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.customerreview.CustomerReviewExcerciseService;
import de.hybris.platform.customerreview.CustomerReviewService;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.customerreview.constants.CustomerReviewConstants;


/**
 * 
 * curse words are defined in customerreview-spring.xml and set directly
 * on curseWords property 
 * Alternatively I think hybris already has some config
 * file which can be used to store these values and code can use 
 * @code de.hybris.platform.util.Config.getParameter(
 *       "cursewords.config.property.name")
 *       
 * Using JaloInvalidParameterException for validation errors as i saw existing code already using JaloXXXException classes 
 * to throw errors with localized messages
 * It assumes below localized properties to be defined in hybris localization files (i guess :-)) 
 * error.customerreview.invalidratingrange = Invalid Review Rating Range. maxRating {0} cannot be less than minRating {1}
 * error.customerreview.invalidratingvalue = Invalid Rating. Rating {0} cannot be less than 0
 * error.customerreview.comment.containscursewords" = Invalid customer Review comment. comment {0} contains cursed words.
 * 
 * Alternatively i could have created own exception classes for this purpose.
 * 
 * I have defined two variants of getNumberOfReviews method one with range provided and one without range reading it from CustomerReviewConstants
 * 
 * @author hsingh
 *
 */
public class CustomerReviewExcerciseServiceImpl implements
		CustomerReviewExcerciseService {

	private CustomerReviewService customerReviewService;

	private String[] curseWords;

	public void setCurseWords(String[] curseWords) {
		this.curseWords = curseWords;
	}

	public String[] getCurseWords() {
		return curseWords;
	}

	protected CustomerReviewService getCustomerReviewService() {
		return this.customerReviewService;
	}

	public void setCustomerReviewService(
			CustomerReviewService customerReviewService) {
		this.customerReviewService = customerReviewService;
	}

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
	public Integer getNumberOfReviews(ProductModel product, Double minRating,
			Double maxRating) throws JaloInvalidParameterException {

		validateCustomerReviewRatingRange(minRating, maxRating);

		List<CustomerReviewModel> customerReviews = customerReviewService
				.getAllReviews();

		return (int) customerReviews
				.stream()
				.filter(review -> (review.getRating() <= maxRating)
						&& (review.getRating() >= minRating)).count();
	}

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
	public Integer getNumberOfReviews(ProductModel product)
			throws JaloInvalidParameterException {
		return getReviewsForProduct(product, CustomerReviewConstants
				.getInstance().getMinRating(), CustomerReviewConstants
				.getInstance().getMaxRating());
	}

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
	public CustomerReviewModel createCustomerReview(Double rating,
			String headline, String comment, UserModel user,
			ProductModel product) throws JaloInvalidParameterException {
        //check for curse words
		validateCustomerReviewCommentForCurseWords(comment);
        
		//check for rating < 0
		validateCustomerReviewCommentForNegativeRating(rating);

		// no exceptions - so create review
		return customerReviewService.createCustomerReview(rating, headline,
				comment, user, product);
	}

	/**
	 * it checks if rating >0 otherwise throws an JaloInvalidParameterException
	 * I could have created my custom exception class for this purpose 
	 * but i saw existing code already using JaloXXXException classes to throw errors with localized messages
	 * 
	 * @param rating
	 * @throws JaloInvalidParameterException
	 */
	protected void validateCustomerReviewCommentForNegativeRating(Double rating)
			throws JaloInvalidParameterException {
		// Checking case rating < 0
		if (rating == null || rating.doubleValue() < 0) {
			throw new JaloInvalidParameterException(
					Localization.getLocalizedString(
							"error.customerreview.invalidratingvalue",
							new Object[] { rating }));
		}
	}

	/**
	 * it checks for maxRating >= minRating with null checks otherwise it throws an exception
	 * I could have created my custom exception class for this purpose 
	 * but i saw existing code already using JaloXXXException classes to throw errors with localized messages
	 * 
	 * @param minRating
	 * @param maxRating
	 * @throws JaloInvalidParameterException
	 */
	protected void validateCustomerReviewRatingRange(Double minRating,
			Double maxRating) throws JaloInvalidParameterException {
		if ((minRating == null) || (maxRating == null)
				|| (maxRating.compareTo(minRating) < 0 )) {
			throw new JaloInvalidParameterException(
					Localization.getLocalizedString(
							"error.customerreview.invalidratingrange",
							new Object[] { minRating, maxRating }));
		}
	}

	/**
	 * It checks if comment contains any curse words defined in customerreview-spring.xml
	 * 
     * Alternatively I could use hybris config file which can be used to store these values and code can use
     * @code de.hybris.platform.util.Config.getParameter(
     *       "cursewords.config.property.name")
	 * 
	 * I could have created my custom exception class for this purpose 
	 * but i saw existing code already using JaloXXXException classes to throw errors with localized messages
	 * 
	 * @param comment
	 * @throws JaloInvalidParameterException
	 */
	protected void validateCustomerReviewCommentForCurseWords(String comment)
			throws JaloInvalidParameterException {
		if (comment != null) {
			String lowerCasedComment = comment.toLowerCase();
			if (Arrays
					.asList(getCurseWords())
					.stream()
					.anyMatch(
							curseword -> lowerCasedComment.contains(curseword))) {
				throw new JaloInvalidParameterException(
						Localization
								.getLocalizedString(
										"error.customerreview.comment.containscursewords",
										new Object[] { comment }));
			}
		}
	}

}
