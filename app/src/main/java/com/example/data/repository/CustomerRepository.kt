package com.example.data.repository

import com.example.data.dao.CustomerDao
import com.example.data.entity.CustomerEntity
import com.example.domain.model.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepository(private val customerDao: CustomerDao) {

    val allCustomers: Flow<List<Customer>> = customerDao.getAllCustomers().map { list ->
        list.map { it.toDomain() }
    }

    val customerCount: Flow<Int> = customerDao.getCustomerCount()

    fun searchCustomers(query: String): Flow<List<Customer>> =
        customerDao.searchCustomers(query).map { list -> list.map { it.toDomain() } }

    suspend fun getCustomerById(id: Long): Customer? =
        customerDao.getCustomerById(id)?.toDomain()

    suspend fun saveCustomer(customer: Customer): Long =
        customerDao.insertCustomer(CustomerEntity.fromDomain(customer))

    suspend fun updateCustomer(customer: Customer) =
        customerDao.updateCustomer(CustomerEntity.fromDomain(customer))

    suspend fun deleteCustomer(customer: Customer) =
        customerDao.deleteCustomer(CustomerEntity.fromDomain(customer))
}
