/*
 * Created On:  4-Sep-07 8:34:51 PM
 */
package com.thinkparity.payment.moneris.delegate;

import java.text.MessageFormat;

import com.thinkparity.common.StringUtil.Separator;

import JavaAPI.HttpsPostRequest;
import JavaAPI.Receipt;
import JavaAPI.XMLable.XMLable;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.profile.payment.PaymentException;

import com.thinkparity.payment.moneris.PaymentEnvironment;
import com.thinkparity.payment.moneris.PaymentTransaction;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class MonerisDelegate {

    /** A payment environment. */
    private PaymentEnvironment environment;

    /** An https post request. */
    private HttpsPostRequest httpsPostRequest;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** An https post request receipt. */
    private Receipt receipt;

    /** A payment transaction. */
    private PaymentTransaction transaction;

    /**
     * Create MonerisDelegate.
     *
     */
    protected MonerisDelegate() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Obtain the payment environment.
     * 
     * @return A <code>PaymentEnvironment</code>.
     */
    public PaymentEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Obtain the receipt.
     * 
     * @return A <code>Receipt</code>.
     */
    public Receipt getReceipt() {
        return receipt;
    }

    /**
     * Obtain the payment transaction.
     * 
     * @return A <code>PaymentTransaction</code>.
     */
    public PaymentTransaction getTransaction() {
        return transaction;
    }

    /**
     * Invoke the moneris delegate.
     * 
     */
    public abstract void invoke() throws Exception;

    /**
     * Set the environment.
     *
     * @param environment
     *      A <code>PaymentEnvironment</code>.
     */
    public void setEnvironment(final PaymentEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Set the transaction.
     *
     * @param transaction
     *      A <code>PaymentTransaction</code>.
     */
    public void setTransaction(final PaymentTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Determine if the receipt contains an invalid pan message.
     * 
     * @param receipt
     *            A <code>Receipt</code>.
     * @return True if the error is due to an invalid pan.
     */
    protected final Boolean isInvalidPan(final Receipt receipt) {
        return receipt.getMessage().equals(Constants.Message.INVALID_PAN);
    }

    /**
     * Panic.
     * 
     * @return A <code>PaymentException</code>.
     */
    protected final PaymentException panic() {
        final Receipt receipt = getReceipt();
        final String message = receipt.getMessage();
        final String iso = receipt.getISO();
        final String referenceNumber = receipt.getReferenceNum();
        final String responseCode = receipt.getResponseCode();
        final String transactionAmount = receipt.getTransAmount();
        final String transactionDate = receipt.getTransDate();
        final String transactionTime = receipt.getTransTime();
        final String transactionType = receipt.getTransType();
        final String transactionNumber = receipt.getTxnNumber();
        return new PaymentException(MessageFormat.format(
                "{3} - {1} - {0}{9}{2} {4} {5} {6} {7} {8}", message, iso,
                referenceNumber, responseCode, transactionAmount,
                transactionDate, transactionTime, transactionType,
                transactionNumber, Separator.SystemNewLine));
    }

    /**
     * Post an xml-able moneris artifact and obtain a receipt.
     * 
     * @param xmlable
     *            An <code>XMLable</code>.
     */
    protected void post(final XMLable xmlable) {
        httpsPostRequest = new HttpsPostRequest(environment.getHost(),
                environment.getStoreId(), environment.getAPIToken(), xmlable);
        receipt = httpsPostRequest.getReceipt();
        logger.logVariable("receipt.getAuthCode()", receipt.getAuthCode());
        logger.logVariable("receipt.getAvsResultCode()", receipt.getAvsResultCode());
        logger.logVariable("receipt.getBankTotals()", receipt.getBankTotals());
        logger.logVariable("receipt.getCardType()", receipt.getCardType());
        logger.logVariable("receipt.getComplete()", receipt.getComplete());
        logger.logVariable("receipt.getCorporateCard()", receipt.getCorporateCard());
        logger.logVariable("receipt.getCvdResultCode()", receipt.getCvdResultCode());
        logger.logVariable("receipt.getISO()", receipt.getISO());
        logger.logVariable("receipt.getMessage()", receipt.getMessage());
        logger.logVariable("receipt.getMessageId()", receipt.getMessageId());
        logger.logVariable("receipt.getReceiptId()", receipt.getReceiptId());
        logger.logVariable("receipt.getRecurSuccess()", receipt.getRecurSuccess());
        logger.logVariable("receipt.getReferenceNum()", receipt.getReferenceNum());
        logger.logVariable("receipt.getResponseCode()", receipt.getResponseCode());
        logger.logVariable("receipt.getTicket()", receipt.getTicket());
        logger.logVariable("receipt.getTimedOut()", receipt.getTimedOut());
        logger.logVariable("receipt.getTransAmount()", receipt.getTransAmount());
        logger.logVariable("receipt.getTransDate()", receipt.getTransDate());
        logger.logVariable("receipt.getTransTime()", receipt.getTransTime());
        logger.logVariable("receipt.getTransType()", receipt.getTransType());
        logger.logVariable("receipt.getTxnNumber()", receipt.getTxnNumber());
    }
}
