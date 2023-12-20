const path = require("path");
const StyleLintPlugin = require('stylelint-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");


module.exports = {
    mode: 'development',
    entry: './frontend/index.ts',
    output: {
        clean: true,
        path: path.resolve(__dirname, './src/main/resources/public/dist'),
        filename: 'bundle.ts',
    },
    resolve: {
        extensions: ['.tsx\', \'.ts\', \'.js\', \'.jsx']
    },

    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                exclude: /node_modules/,
                use: 'ts-loader',
            },
            {
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, 'css-loader']
            },
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                use: 'babel-loader',
            },
        ],
    },
    plugins: [new StyleLintPlugin({
        configFile: '.stylelint.json',
        context: './frontend/styles',
        files: '**/*.css',
    }),
        new ESLintPlugin(),
        new MiniCssExtractPlugin()
    ]
}