/*
 * May 15, 2005
 */
package com.thinkparity.model.parity;


public class ParityShutdownHook extends Thread {

	public ParityShutdownHook() {
		super();
		// TODO:  Fill in abstract construction.
	}

	public ParityShutdownHook(Runnable target) {
		super(target);
		// TODO:  Fill in abstract construction.
	}

	public ParityShutdownHook(ThreadGroup group, Runnable target) {
		super(group, target);
		// TODO:  Fill in abstract construction.
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
	}

	public ParityShutdownHook(String name) {
		super(name);
		// TODO:  Fill in abstract construction.
	}

	public ParityShutdownHook(ThreadGroup group, String name) {
		super(group, name);
		// TODO:  Fill in abstract construction.
	}

	public ParityShutdownHook(Runnable target, String name) {
		super(target, name);
		// TODO:  Fill in abstract construction.
	}

	public ParityShutdownHook(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
		// TODO:  Fill in abstract construction.
	}

	public ParityShutdownHook(ThreadGroup group, Runnable target, String name,
			long stackSize) {
		super(group, target, name, stackSize);
		// TODO:  Fill in abstract construction.
	}

}
