package com.kanyandula.malawi.api

import com.kanyandula.malawi.data.model.Blog
import com.kanyandula.malawi.data.util.DomainMapper

class BlogDtoMapper : DomainMapper<BlogDto, Blog> {

    override fun mapToDomainModel(model: BlogDto): Blog {
       return Blog(
           title = model.title,
           date =  model.date,
           desc = model.desc,
           image = model.image,
           uid = model.uid,
           userName = model.userName,
           timestamp = model.timestamp,
           favorite = model.favorite
       )
    }


    /**
     * We use this function when publishing to the network
     */
    override fun mapFromDomainModel(domainModel: List<Blog>): BlogDto {
        return BlogDto(
            title = domainModel[0].title,
            date =  domainModel[1].date,
            desc = domainModel[2].desc,
            image = domainModel[3].image,
            uid = domainModel[4].uid,
            userName = domainModel[5].userName,
            timestamp = domainModel[6].timestamp,
            favorite = domainModel[7].favorite
        )
    }

    fun  toDomainList(initial: List<BlogDto>): List<Blog>{
        return  initial.map {mapToDomainModel(it) }
    }

}