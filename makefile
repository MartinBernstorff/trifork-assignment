build:
	./gradlew build

test:
	./gradlew test

run:
	./gradlew bootRun

ci_test:
	docker build . -t trifork-ci -f Dockerfile