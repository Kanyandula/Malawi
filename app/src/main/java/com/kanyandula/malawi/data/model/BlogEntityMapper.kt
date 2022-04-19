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
          favorite = model.favorite,


      )
    }





    override fun mapFromDomainModel(domainModel: BlogDto): Blog {

            return Blog(
                title = domainModel.title,
                date = domainModel.date,
                desc = domainModel.desc,
                image = domainModel.image,
                uid = domainModel.uid,
                userName = domainModel.userName,
                timestamp = domainModel.timestamp,
                favorite = domainModel.favorite,
            )
        }


    fun fromEntityList(initial: List<Blog>): List<BlogDto>{
        return initial.map { mapToDomainModel(it) }
    }



    fun  toEntityList(initial: List<BlogDto>): List<Blog>{
        return initial.map { mapFromDomainModel(it) }
    }



}