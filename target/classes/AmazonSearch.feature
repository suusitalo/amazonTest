Feature: Amazon camera search test
	
Scenario Outline: 	Find second Nikon model from list which has been sorted by most expensive to cheapest 
					and check if the description contains Nikon D3X 
					
Given I open web browser and navigate to '<Url>' 

When I search for Nikon
	
Then I should be able to sort results by price high-to-low
	And I should be able to select second camera from the list
	And I should see 'Nikon D3X' in the product description topic

Then I close the browser

Examples:
	| Url 				|
	| www.amazon.com	|
	