FROM ghcr.io/graalvm/native-image-community:21 AS build
RUN microdnf install findutils
COPY . /app
WORKDIR /app
RUN ./gradlew nativeCompile

FROM seepine/alpine-glibc
COPY --from=build /app/build/native/nativeCompile/compliment-bot /native/compliment-bot
WORKDIR /native
CMD [ "./compliment-bot" ]