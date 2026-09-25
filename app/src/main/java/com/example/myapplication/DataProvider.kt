package com.example.myapplication

object DataProvider {
    val categories = listOf("Tất cả", "Đồ ăn nhanh", "Món Việt", "Đồ uống", "Tráng miệng")

    val allDishes = listOf(
        Dish("d1", "Hamburger bò", 55000, "Bánh mì kẹp thịt bò nướng, xà lách, cà chua", "Bánh mì, thịt bò, rau xà lách, sốt phô mai", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd", "Đồ ăn nhanh"),
        Dish("d2", "Pizza hải sản", 120000, "Pizza với tôm, mực, sốt cà chua và phô mai", "Bột mì, tôm, mực, phô mai mozzarella", "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38", "Đồ ăn nhanh"),
        Dish("d3", "Mì cay", 65000, "Mì cay Hàn Quốc với xúc xích, kim chi", "Mì Hàn Quốc, xúc xích, kim chi, chả cá", "https://images.unsplash.com/photo-1585032226651-759b368d7246", "Khác"),
        Dish("d4", "Gà rán", 75000, "Gà rán giòn rụm với sốt chua ngọt", "Thịt gà, bột chiên giòn, tương ớt", "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec", "Đồ ăn nhanh"),
        Dish("d5", "Trà sữa", 35000, "Trà sữa trân châu đường đen", "Trà đen, sữa, trân châu, đường đen", "https://images.unsplash.com/photo-1558855567-3315752edce5", "Đồ uống"),
        Dish("d6", "Phở Bò Tái", 55000, "Gầu dòn, nước trong, hành chần...", "Bánh phở, thịt bò, hành lá", "https://images.unsplash.com/photo-1582878826629-29b7ad1cb438", "Món Việt"),
        Dish("d7", "Cơm Tấm Sườn", 60000, "Sườn nướng than, bì chả...", "Cơm tấm, sườn nướng, chả trứng", "https://images.unsplash.com/photo-1555126634-323283e090fa", "Món Việt"),
        Dish("d8", "Bún Chả Hà Nội", 60000, "Chả nướng, nem cua bể...", "Bún, chả nướng, nước mắm", "https://images.unsplash.com/photo-1583337130417-3346a1be7dee", "Món Việt"),
        Dish("d9", "Sinh Tố Xoài", 30000, "Xoài cát hòa lộc xay nhuyễn", "Xoài, sữa đặc, đá xay", "https://images.unsplash.com/photo-1628174542289-4b68cebb6571", "Đồ uống"),
        Dish("d10", "Chè Khúc Bạch", 45000, "Tráng miệng thanh mát", "Sữa tươi, gelatin, nhãn, hạnh nhân", "https://images.unsplash.com/photo-1551024601-bec78aea704b", "Tráng miệng")
    )

    fun getDishById(id: String): Dish? {
        return allDishes.find { it.id == id }
    }
}