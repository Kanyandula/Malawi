package com.kanyandula.malawi.data.model

import com.kanyandula.malawi.api.BlogDto
import com.kanyandula.malawi.data.util.DomainMapper

class BlogEntityMapper : DomainMapper<Blog, BlogDto> {

    override fun mapToDomainModel(model: Blog): BlogDto {
      return BlogDto(

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

    override fun mapFromDomainModel(domainModel: List<BlogDto>): Blog {
       return Blog(

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

    fun fromEntityList(initial: List<Blog>): List<BlogDto>{
        return initial.map { mapToDomainModel(it) }
    }

//    fun  toEntityList(initial: List<BlogDto>): List<Blog>{
//        return initial.map { mapFromDomainModel(it) }
//    }



}