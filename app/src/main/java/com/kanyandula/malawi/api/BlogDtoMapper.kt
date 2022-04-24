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
           favorite = model.favorite,

       )
    }


    /**
     * We use this function when publishing to the network
     */


    override fun mapFromDomainModel(domainModel: Blog): BlogDto {
        return BlogDto(
            title = domainModel.title,
            date =  domainModel.date,
            desc = domainModel.desc,
            image = domainModel.image,
            uid = domainModel.uid,
            userName = domainModel.userName,
            timestamp = domainModel.timestamp,
            favorite = domainModel.favorite,
        )
    }

    fun  toDomainList(initial: List<BlogDto>): List<Blog>{
        return  initial.map {mapToDomainModel(it) }
    }

}