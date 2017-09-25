Resource: {
  player_wallet_binding, // 每个结构为 {id, player_id, currency_type, currency_value}
  player_wallet, // 1 <=> n  player_wallet_binding
  sku, // a.k.a. "最小可购买单元" 每个结构为 {sku_id, sku_name}
  shop_sku_price_binding, // 每个结构为 {sku_id, currency_type, currency_value, shop_id} --不同shop的同一sku_id是否同价？
  trade, // a.k.a. "订单"，自生成的一刻起就会预留订单内sku的库存，直至付费或消费，每个结构为 {id} --"订单“是否支持落不同shop_id的单？
  trade_sku_binding, // 每个结构为 {trade_id,,shop_id,sku_id, amount}
  shop, // a.k.a. "商铺" 每个结构为 {shop_id,shop_name,shop_addr,shop_longitude ,shop_latitude，shop_role_id}
  role, // a.k.a "角色“ 每个结构为{ role_id ,role_name}
  shop_sku_amount_binding, // 每个结构为{shop_id, sku_id, balance, reserved_balance}
  player_shopping_cart_sku ,// 每个结构为{player_shopping_cart_id, player_id,shop_id,sku_id, amount}
}



