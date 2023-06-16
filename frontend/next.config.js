/** @type {import('next').NextConfig} */

const path = require("path");

const nextConfig = {
  webpack: (config, { isServer }) => {
    // Add aliases for directories in the src folder
    config.resolve.alias["@components"] = path.join(
      __dirname,
      "src/components"
    );
    config.resolve.alias["@styles"] = path.join(__dirname, "src/styles");
    return config;
  },
  reactStrictMode: true,
  eslint: { ignoreDuringBuilds: true },
};

module.exports = nextConfig;
