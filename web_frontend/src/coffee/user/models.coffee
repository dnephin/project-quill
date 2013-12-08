

QuillApp.User = DS.Model.extend

    links:          DS.attr()
    firstName:      DS.attr('string')
    lastName:       DS.attr('string')
    fullName:       DS.attr('string')
    avatarUrl:      DS.attr('string')

    statements:     DS.hasMany('statement')
