async function fetchProducts() {
  try {
    const response = await fetch('http://localhost:8080/api/v1/products');

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    const data = await response.json();
    return data;

  } catch (error) {
    console.error('Fetch error:', error.message);
    throw error;
  }
}

async function renderProducts() {
  const main = document.querySelector('main');
  
  try {
    const products = await fetchProducts();

    if (products.length === 0) {
      main.innerHTML = <p>No products found.</p>;
      return;
    }

    const productGrid = `
      <div class="product-grid">
        ${products.map(product =>`
          <div class="product-card">
            <img src="${product.imageUrl}" alt="${product.name}">
            <h3>${product.name}</h3>
            <p>Price: $${product.price.toFixed(2)}</p>
            <p>Stock: ${product.stockQuantity}</p>
          </div>
        `).join('')}
      </div>
    `;

    main.innerHTML = productGrid;

  } catch (error) {
    main.innerHTML = <p>Failed to load products. Check console.</p>;
  }
}

document.addEventListener('DOMContentLoaded', renderProducts);