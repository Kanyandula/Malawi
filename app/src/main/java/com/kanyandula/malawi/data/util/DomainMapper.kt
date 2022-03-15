package com.kanyandula.malawi.data.util

import com.kanyandula.malawi.api.BlogDto

interface DomainMapper <T, DomainModel>{

    fun mapToDomainModel(model: T): DomainModel

    fun mapFromDomainModel(domainModel: List<DomainModel>): T


}