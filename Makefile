project_name = service-discovery
override registry_docker_tag = ${project_name}-registry

main:
	echo "Please specify what should be build: 'bootRun_registry', 'build_client', 'build_registry', 'build_registry_image'"

bootRun_registry:
	./gradlew clean :registry:bootRun

build_client:
	./gradlew clean :client:build

build_registry:
	./gradlew clean :registry:build

build_registry_image:
	docker build -t ${registry_docker_tag} -f ./registry/Dockerfile .

