function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples
n = length(theta);

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta

predict = sigmoid(X * theta);
temp = -y .* log(predict) - (1 - y) .* log(1 - predict);
J = 1 / m * sum(temp) + lambda / (2 * m) * sum(theta(2:n) .^ 2);

predict = predict - y;
theta0 = 1 / m * (predict' * X(:, 1)); % do not regularize theta(0)

grad = 1 / m * (predict' * X)' + lambda / m * theta;
grad(1, 1) = theta0;

% =============================================================

end
