package com.aje.onemenu;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String FAKE_STRING = "HELLO_WORLD";
   // private Context context = ApplicationProvider.getApplicationContext();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void readStringFromContext_LocalizedString() {
        // Given a Context object retrieved from Robolectric...
       // ClassUnderTest myObjectUnderTest = new ClassUnderTest(context);

        // ...when the string is returned from the object under test...
      //  String result = myObjectUnderTest.getHelloWorldString();

        // ...then the result should be the expected one.
       // assertThat(result).isEqualTo(FAKE_STRING);
    }

}



