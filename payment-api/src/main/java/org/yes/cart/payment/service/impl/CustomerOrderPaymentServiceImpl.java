/*
 * Copyright 2009 Igor Azarnyi, Denys Pavlov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.yes.cart.payment.service.impl;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.yes.cart.payment.persistence.entity.CustomerOrderPayment;
import org.yes.cart.payment.persistence.service.PaymentModuleGenericDAO;
import org.yes.cart.payment.service.CustomerOrderPaymentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 07-May-2011
 * Time: 10:22:53
 */
public class CustomerOrderPaymentServiceImpl
        extends PaymentModuleGenericServiceImpl<CustomerOrderPayment>
        implements CustomerOrderPaymentService {

    private static final int DEFAULT_SCALE = 2;

    /**
     * Construct service to work with payments
     *
     * @param genericDao dao to use.
     */
    public CustomerOrderPaymentServiceImpl(final PaymentModuleGenericDAO<CustomerOrderPayment, Long> genericDao) {
        super(genericDao);
    }

    /**
     * Get order amount.
     *
     * @param orderNumber given order number.
     * @return order amount
     */
    public BigDecimal getOrderAmount(final String orderNumber) {
        BigDecimal rez = BigDecimal.ZERO;
        final List<CustomerOrderPayment> payments = findBy(orderNumber, null, null, null);
        for (CustomerOrderPayment payment : payments) {
            rez = rez.add(payment.getPaymentAmount());
        }
        return rez.setScale(DEFAULT_SCALE);
    }


    /**
     * {@inheritDoc}
     */
    public List<CustomerOrderPayment> findBy(
            final String orderNumber,
            final String shipmentNumber,
            final String paymentProcessorResult,
            final String transactionOperation) {

        final ArrayList<Criterion> creterias = new ArrayList<Criterion>(4);

        if (orderNumber != null) {
            creterias.add(Restrictions.eq("orderNumber", orderNumber));
        }

        if (shipmentNumber != null) {
            creterias.add(Restrictions.eq("orderShipment", shipmentNumber));
        }

        if (paymentProcessorResult != null) {
            creterias.add(Restrictions.eq("paymentProcessorResult", paymentProcessorResult));
        }

        if (transactionOperation != null) {
            creterias.add(Restrictions.eq("transactionOperation", transactionOperation));
        }

        return getGenericDao().findByCriteria(
                creterias.toArray(new Criterion[creterias.size()])

        );
    }


}
