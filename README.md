# product-compare-service
this is just a demo service with some basic requests. There are 2 services. 1 will handle product information. 2 will handle product review


I. HIGHLIGHT FEATURES
	1. Requests are redirected to SSL to ensure security in data transfer
	2. All requests/reponses have been logged in log file. Request/Responses have been well formatted using custom formatter classes, so user can easily review the log. All log files are stored in logs folder
	3. Use thymeleaf to redirect error.html page - just for fun :)
	4. Use lombok because I am lazy to generate getter/setter	
	5. Use Cachable annotation for better performance and reduce number to request to 3rd service (simulate backend call time (3s) by Thread.sleep to check time to respond)
	6. Use bucket4j to limit request of user in 1 minute (currently set 5 requests/1 minute)
	7. Use RestTemplate to call between services


II. DEPLOYMENT STEPS
	Option 1: Directly use maven to start services from projects
		cd to service folder and run
		mvnw spring-boot:run
	Option 2: Open folder (jar-files) containing 2 jar files and do below steps
		2.1 Import crt file into cacerts file of java (to avoid SSL handshake issue between 2 services)
			a. Run cmd as Administrator
			b. cd C:\Program Files\Java\jre1.8.0_281\lib\security
			c. Copy nbaCert.crt file (under keys folder) into this directory
			d. Run: keytool -import -keystore cacerts -file nbaCert.crt
			e. Enter default password of cacerts: changeit
		2.2 Run Fetching product service
			Run: java -jar fetch-product-service-0.0.1-SNAPSHOT.jar
		2.3 Run Product Review service
			Run: jaav -jar product-review-service-0.0.1-SNAPSHOT.jar
		*** Please make sure that folder keys is in the same folder with 2 jar files
	
			
III. ALL CURL COMMANDS TO VERIFY 2 SERVICES
	since this demo is simply get production and compare information, there is only GET request is implemented

	Fetching product service

		Get All Products
		curl -v --insecure https://localhost:8443/api/v1/products

		Get Product by ID
		curl -v --insecure https://localhost:8443/api/v1/product/1
		curl -v --insecure https://localhost:8443/api/v1/product/2
		curl -v --insecure https://localhost:8443/api/v1/product/3

		Get Review for Product by ID
		curl -v --insecure https://localhost:8443/api/v1/product-review/1
		curl -v --insecure https://localhost:8443/api/v1/product-review/2
		curl -v --insecure https://localhost:8443/api/v1/product-review/3

	Product Review Service
		
		Get Review for Product by ID. Fetching product service will call this service to get product review
		curl -v --insecure https://localhost:8444/api/v1/product-review/1
		curl -v --insecure https://localhost:8444/api/v1/product-review/2
		curl -v --insecure https://localhost:8444/api/v1/product-review/3


IV. APPENDIX

	Some functions are written just for demo. We need to enhance these functions in real environment
	NO#			FUNCTION							IN DEMO																					ENHANCE NEEDED
	1			Returned data for all requests		Prepare a pre-defined list to return to client											Update to connect to 3rd vendor to retrieve real data
	2			getProductById						Just loop in pre-defined list															Use jpa interface such as JpaRepository to work with DB Or send request to 3rd vendor to retrieve real data
	3			Using SSL							Get warning about certificate.															Extract SSL certificate and import to client site
													Override current hostname verifier to bypass the SSL connection issue
	
	4			Handling exception 					There is not many handling exception in this demo due to simple services and requests	We need to handle more exception based on the complexity of real data and service tasks.
			
