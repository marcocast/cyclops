package com.aol.cyclops.trycatch;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class SuccessTest {

	Success<Integer,FileNotFoundException> success;
	final Integer value = 10;
	
	@Before
	public void setup(){
		success = Success.of(10);
	}

	@Test
	public void testUnapply() {
		assertThat(success.unapply(),equalTo(Arrays.asList(value)));
	}

	@Test
	public void testGet() {
		assertThat(success.get(),equalTo(value));
	}

	@Test
	public void testOf() {
		assertThat(value,not(nullValue()));
	}

	@Test
	public void testMap() {
		assertThat(success.map(x->x+1),equalTo(Success.of(value+1)));
	}

	@Test
	public void testFlatMap() {
		assertThat(success.flatMap(x->Success.of(x+1)),equalTo(Success.of(value+1)));
	}

	@Test
	public void testFilter() {
		assertThat(success.filter(x->x>5),equalTo(Optional.of(value)));
	}
	@Test
	public void testFilterFail() {
		assertThat(success.filter(x->x>15),equalTo(Optional.empty()));
	}

	@Test
	public void testRecover() {
		assertThat(success.recover(e->20),equalTo(success));
	}

	@Test
	public void testRecoverWith() {
		assertThat(success.recoverWith(e->Success.of(20)),equalTo(success));
	}

	@Test
	public void testRecoverFor() {
		assertThat(success.recoverFor(IOException.class,e->20),equalTo(success));
	}

	@Test
	public void testRecoverWithFor() {
		assertThat(success.recoverWithFor(FileNotFoundException.class,e->Success.of(20)),equalTo(success));
	}

	@Test
	public void testFlatten() {
		
		assertThat(Success.of(success).flatten(),equalTo(success));
	}
	@Test
	public void testFlattenFailure() {
		FileNotFoundException error = new FileNotFoundException();
		assertThat(Success.of(Failure.of(error)).flatten(),equalTo(Failure.of(error)));
	}

	@Test
	public void testOrElse() {
		assertThat(success.orElse(30),equalTo(value));
	}

	@Test
	public void testOrElseGet() {
		assertThat(success.orElseGet(()->30),equalTo(value));
	}

	@Test
	public void testToOptional() {
		assertThat(success.toOptional(),equalTo(Optional.of(value)));
	}

	@Test
	public void testToStream() {
		assertThat(success.stream().collect(Collectors.toList()),
				equalTo(Stream.of(value).collect(Collectors.toList())));
	}

	@Test
	public void testIsSuccess() {
		assertTrue(success.isSuccess());
	}

	@Test
	public void testIsFailure() {
		assertFalse(success.isFailure());
	}
	Integer valueCaptured = null;
	@Test
	public void testForeach() {
		success.foreach(v -> valueCaptured = v);
		assertThat(valueCaptured,is(10));
	}
	Exception errorCaptured;
	@Test
	public void testOnFailConsumerOfX() {
		errorCaptured = null;
		success.onFail(e -> errorCaptured =e);
		assertThat(errorCaptured,is(nullValue()));
	}

	@Test
	public void testOnFailClassOfQsuperXConsumerOfX() {
		errorCaptured = null;
		success.onFail(IOException.class,e -> errorCaptured =e);
		assertThat(errorCaptured,is(nullValue()));
	}

	@Test
	public void testThrowException() {
		success.throwException();
	}

	@Test
	public void testToFailedOptional() {
		
		assertThat(success.toFailedOptional(),is(Optional.empty()));
	}

	@Test
	public void testToFailedStream() {
		assertThat(success.toFailedStream().findFirst(),is(Optional.empty()));
	}

	@Test
	public void testForeachFailed() {
		errorCaptured = null;
		success.foreachFailed(e -> errorCaptured =e);
		assertThat(errorCaptured,is(nullValue()));
	}


}
