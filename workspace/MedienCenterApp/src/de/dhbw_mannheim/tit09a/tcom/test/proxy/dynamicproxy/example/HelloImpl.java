package de.dhbw_mannheim.tit09a.tcom.test.proxy.dynamicproxy.example;

public class HelloImpl implements IHello
{

    @Override
    public String sayHello(String name)
    {
	return "Hello " + name + "!";
    }
}
