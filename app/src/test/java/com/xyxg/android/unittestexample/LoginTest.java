package com.xyxg.android.unittestexample;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;


/**
 * @author YML
 * @date 2016/9/6
 */
public class LoginTest {
    private Login login;
    Request request;

    @Mock
    Request request2;

    @Before
    public void setUp() {
        login = new Login();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loginTest() {
        request = Mockito.mock(Request.class);
        login.setRequest(request);
        String un = "ddd";
        String pwd = "11";
        //Mockito.when(request.check(Mockito.anyString())).thenReturn("password");
        Mockito.when(request.check(Mockito.startsWith("11"))).thenReturn(pwd);
        login.login(un, pwd);
        Mockito.verify(request).login(un, pwd);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(request).check(captor.capture());

        assertEquals("11", captor.getValue());

        InOrder inOrder = Mockito.inOrder(request);
        inOrder.verify(request).check(pwd);
        inOrder.verify(request).login(un, pwd);
    }

    @Test
    public void loginTest1() {
        Request request1 = Mockito.spy(Request.class);
        login.setRequest(request1);
        String un = "ddd";
        String pwd = "11";
        //Mockito.when(request.check(Mockito.anyString())).thenReturn("password");
       // Mockito.when(request1.check(anyString())).thenReturn("11");
        login.login(un, pwd);
        Mockito.verify(request1).login(un, pwd);
    }

    @Test
    public void loginDaggerTest() {
        request = Mockito.mock(Request.class);
        //DaggerAppComponent.builder().appModules(new AppModules().provideRequest()).build().inject(login);
        login.login("ddd", "11");
    }
}
