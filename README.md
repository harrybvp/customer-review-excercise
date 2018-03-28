# Overview of Solution
1. customerReviewExcerciseService is the bean name defined under [customerreview-spring.xml](customerreview-spring.xml)  implements the requirment given in the assignment.
1. [CustomerReviewExcerciseService](customerreviewserver.jar.src/de/hybris/platform/customerreview/CustomerReviewExcerciseService.java) is the interface and [CustomerReviewExcerciseServiceImpl](customerreviewserver.jar.src/de/hybris/platform/customerreview/CustomerReviewExcerciseServiceImpl.java) is it's implementation (Refer it's JavaDoc for implementation details)
1. [Curse Words](https://github.com/harrybvp/customer-review-excercise/blob/e505fd8e200df908849f513da6d47764fff16487/customerreview-spring.xml#L47) are defined in [customerreview-spring.xml](customerreview-spring.xml) and set directly on curseWords property.
Alternatively I think hybris already has some config file which can be used to store these values and code can use `de.hybris.platform.util.Config.getParameter("cursewords.config.property.name")`
1. I have defined two variants of `getNumberOfReviews` method one with range provided and one without range reading it from `CustomerReviewConstants`
1. Solution is using `JaloInvalidParameterException` for validation errors as i saw existing code already using JaloXXXException classes to throw errors with localized messages.<br>It assumes below localized properties to be defined in hybris localization files.  
````
error.customerreview.invalidratingrange = Invalid Review Rating Range. maxRating {0} cannot be less than minRating {1}
error.customerreview.invalidratingvalue = Invalid Rating. Rating {0} cannot be less than 0
error.customerreview.comment.containscursewords" = Invalid customer Review comment. comment {0} contains cursed words.
 ````  
 Alternatively i could have created own exception classes for this purpose.

