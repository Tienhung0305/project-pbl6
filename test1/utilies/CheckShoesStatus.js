import { images, icons, fontSize, colors } from '../constants/index'
function getColorFromStatus(status) {
    // if (status.toLowerCase().trim() == 'closing soon') {
    //     return colors.alert
    // } else if (status.toLowerCase().trim() == 'openning soon') {
    //     return colors.success
    // } else if (status.toLowerCase().trim() == 'comming soon') {
    //     return colors.warnning
    // }

    return status.toLowerCase().trim() == 'closing soon' ? colors.alert :
        (status.toLowerCase().trim() == 'openning soon' ? colors.success :
            (status.toLowerCase().trim() == 'comming soon' ? colors.warnning : colors.success))


}
export default getColorFromStatus