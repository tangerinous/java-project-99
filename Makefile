ci:
	docker-compose -f docker-compose.yml run app make setup
	docker-compose -f docker-compose.yml up --abort-on-container-exit

compose-setup: compose-build compose-app-setup

compose-build:
	docker-compose build

compose-app-setup:
	docker-compose run --rm app make setup

compose-bash:
	docker-compose run --rm --service-ports app bash

compose-lint:
	docker-compose run --rm app make lint

compose-test:
	docker-compose -f docker-compose.yml up --abort-on-container-exit

compose:
	docker-compose up

compose-down:
	docker-compose down -v --remove-orphans

setup:
	cd code/app && ./gradlew clean build

test:
	gradle test

lint:
	gradle checkstyleTest checkCode

code-start:
	make/app -C app start

check-updates:
	gradle dependencyUpdates

deploy:
	git subtree push --prefix code heroku main